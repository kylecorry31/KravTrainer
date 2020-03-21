package com.kylecorry.kravtrainer.infrastructure

import com.kylecorry.kravtrainer.domain.models.Acceleration
import java.util.*

class BluetoothGloves: Observable(), SerialListener {

    private val serial = BluetoothSerial("98:D3:11:FC:47:C4")
    private var count = 0

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

    fun start(){
        if (started) return
        started = true
        serial.registerListener(this)
        serial.connect()
    }

    fun stop(){
        if (!started) return
        started = false
        serial.disconnect()
        serial.unregisterListener(this)
    }

    override fun onData(data: String) {
        val strValues = data.split(",")

        if (strValues.size != 4 || strValues.any { it.isEmpty() }){
            return
        }

        val values = strValues.map { it.toFloat() }

        val acceleration = Acceleration(values[1], values[2], values[3])

        if (values[0] == 0f){
            left = acceleration
        } else {
            right = acceleration
        }

    }

    override fun onConnect() {
        isConnected = true
        println("CONNECTED")
    }

    override fun onDisconnect() {
        isConnected = false
        println("DISCONNECTED")
    }


}