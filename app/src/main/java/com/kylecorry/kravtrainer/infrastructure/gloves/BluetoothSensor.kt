package com.kylecorry.kravtrainer.infrastructure.gloves

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import com.kylecorry.trailsensecore.infrastructure.sensors.AbstractSensor
import java.io.*
import java.time.Instant
import java.util.*
import kotlin.concurrent.thread

class BluetoothSensor(private val device: BluetoothDevice, private val messageHistoryLength: Int):
    AbstractSensor(), IBluetoothSensor {

    override val hasValidReading: Boolean
        get() = messages.isNotEmpty()

    override fun startImpl() {
        connect()
    }

    override fun stopImpl() {
        disconnect()
    }

    private var socket: BluetoothSocket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    override val messages: List<BluetoothMessage>
        get() = _messages

    override val isConnected: Boolean
        get() = socket?.isConnected == true

    private val _messages = mutableListOf<BluetoothMessage>()
    private val handler = Handler(Looper.getMainLooper())

    private fun connect(){
        thread {
            try {
                socket =
                    device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                socket?.connect()
                input = socket?.inputStream
                output = socket?.outputStream
            } catch (e: IOException) {
                socket?.close()
                e.printStackTrace()
            }

            if (isConnected){
                startInputListener()
            }

            handler.post {
                notifyListeners()
            }
        }
    }

    private fun disconnect(){
        if (isConnected){
            input?.close()
            output?.close()
            socket?.close()
        }

        socket = null
        input = null
        output = null

        notifyListeners()
    }

    override fun write(data: String): Boolean {
        if (!isConnected) return false
        val bytes = data.toByteArray()
        try {
            output?.write(bytes)
            return true
        } catch (e: IOException){
            e.printStackTrace()
        }
        return false
    }

    private fun startInputListener(){
        thread {
            val inputStream = input!!

            val reader = BufferedReader(InputStreamReader(inputStream))

            while (isConnected){
                synchronized(inputStream){
                    try {
                        val recv = reader.readLine()
                        val message = BluetoothMessage(recv, Instant.now())
                        _messages.add(message)
                        while(_messages.size > messageHistoryLength){
                            _messages.removeAt(0)
                        }
                        handler.post { notifyListeners() }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }

            handler.post { notifyListeners() }

        }
    }

}