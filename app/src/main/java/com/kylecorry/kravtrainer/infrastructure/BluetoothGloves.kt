package com.kylecorry.kravtrainer.infrastructure

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import com.kylecorry.kravtrainer.domain.models.Acceleration
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class BluetoothGloves: Observable() {

    private val address = "98:D3:11:FC:47:C4"
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    private val MESSAGE_READ = 1
    private val CONNECTION_STATUS = 2


    var left: Acceleration = Acceleration.zero
        private set(value) {
            setChanged()
            notifyObservers()
            field = value
        }

    var right: Acceleration = Acceleration.zero
        private set(value){
            setChanged()
            notifyObservers()
            field = value
        }

    var isConnected: Boolean = true
        private set(value){
            setChanged()
            notifyObservers()
            field = value
        }

    private var started = false

    private var handler: Handler? = null
    private var connectedThread: ConnectedThread? = null

    // TODO: Register bluetooth listener
    fun start(){
        if (started) return
        started = true

        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_READ){
                    var message: String? = null
                    try {
                        message = String(msg.obj as ByteArray)
                        println(message)
                    } catch (e: UnsupportedEncodingException){}
                }

                if (msg.what == CONNECTION_STATUS){
                    if (msg.arg1 == 1){
                        println("CONNECTED")
                    } else {
                        println("NOT CONNECTED")
                    }
                }

            }
        }

        thread {
            val device = adapter.getRemoteDevice(address)

            try {
                socket = device.createRfcommSocketToServiceRecord( UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"))
            } catch (e: IOException){
                e.printStackTrace()
            }

            try {
                socket?.connect()
            } catch (e: IOException){
                socket?.close()
            }

            if (socket?.isConnected == true){
                connectedThread = ConnectedThread(socket!!)
                connectedThread?.start()
            }
        }
    }

    fun stop(){
        if (!started) return
        started = false
        handler = null
        connectedThread?.cancel()
        connectedThread = null
    }


    private inner class ConnectedThread(private val socket: BluetoothSocket): Thread() {

        private var inputStream: InputStream? = null
        private var outputStream: OutputStream? = null

        init {
            try {
                inputStream = socket.inputStream
                outputStream = socket.outputStream
            } catch (e: IOException){
                // Notify user
            }
        }

        override fun run() {
            super.run()
            val buffer = ByteArray(1024)
            var bytes: Int

            while (started){
                try {
                    bytes = inputStream?.available() ?: 0

                    if (bytes != 0){
                        inputStream?.read(buffer, bytes, buffer.size - bytes)

                        println(buffer.toString(Charset.forName("UTF-8")))
                    }

                } catch (e: IOException){

                }
            }
        }

        fun write(input: String){
            val bytes = input.toByteArray()
            try {
                outputStream?.write(bytes)
            } catch (e: IOException){}
        }

        fun cancel(){
            try {
                socket.close()
            } catch (e: IOException){}
        }

    }


}