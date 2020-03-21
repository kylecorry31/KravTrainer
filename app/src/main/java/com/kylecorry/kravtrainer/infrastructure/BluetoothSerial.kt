package com.kylecorry.kravtrainer.infrastructure

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import java.io.*
import java.util.*
import kotlin.concurrent.thread

class BluetoothSerial(private val address: String) {

    private val listeners = mutableListOf<SerialListener>()
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    /**
     * Connect to the bluetooth device
     */
    fun connect(){
        thread {
            val device = adapter.getRemoteDevice(address)

            try {
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                socket?.connect()
                input = socket?.inputStream
                output = socket?.outputStream
            } catch (e: IOException){
                socket?.close()
                e.printStackTrace()
            }

            if (socket?.isConnected == true){
                Handler(Looper.getMainLooper()).post {
                    listeners.forEach { it.onConnect() }
                }
                startInputListener()
            }
        }
    }

    /**
     * Disconnect from the bluetooth device
     */
    fun disconnect(){
        if (socket?.isConnected == true){
            input?.close()
            output?.close()
            socket?.close()
        }

        socket = null
        input = null
        output = null

        listeners.forEach { it.onDisconnect() }
    }

    /**
     * Write data to the bluetooth device
     */
    fun write(data: String): Boolean {
        if (socket?.isConnected != true) return false
        val bytes = data.toByteArray()
        try {
            output?.write(bytes)
            return true
        } catch (e: IOException){
            e.printStackTrace()
        }
        return false
    }

    /**
     * Add an incoming data listener
     */
    fun registerListener(listener: SerialListener){
        if (listeners.contains(listener)) return
        listeners.add(listener)
    }

    /**
     * Remove an incoming data listener
     */
    fun unregisterListener(listener: SerialListener){
        listeners.remove(listener)
    }

    private fun startInputListener(){
        thread {

            val inputStream = input!!
            val handler = Handler(Looper.getMainLooper())

            val reader = BufferedReader(InputStreamReader(inputStream))

            while (socket?.isConnected == true){
                synchronized(inputStream){
                    try {
                        val recv = reader.readLine()
                        handler.post { listeners.forEach { it.onData(recv) } }
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }

            handler.post { listeners.forEach { it.onDisconnect() }}

        }
    }

}