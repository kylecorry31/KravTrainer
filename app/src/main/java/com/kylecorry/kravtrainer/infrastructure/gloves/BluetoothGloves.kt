package com.kylecorry.kravtrainer.infrastructure.gloves

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.kravtrainer.domain.classifiers.PunchClassifierFactory
import java.util.*

class BluetoothGloves(address: String): Observable(),
    SerialListener {

    private val serial =
        BluetoothSerial(address)
    private val leftPunchClassifier = PunchClassifierFactory.createPunchClassifier()
    private val rightPunchClassifier = PunchClassifierFactory.createPunchClassifier()

    var left: PunchType? = null
        private set(value) {
            setChanged()
            notifyObservers()
            field = value
        }

    var right: PunchType? = null
        private set(value){
            setChanged()
            notifyObservers()
            field = value
        }

    var leftStrength: Float = 0f
        private set(value){
            setChanged()
            notifyObservers()
            field = value
        }

    var rightStrength: Float = 0f
        private set(value){
            setChanged()
            notifyObservers()
            field = value
        }

    val isConnected: Boolean
        get() = serial.isConnected

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
            left = leftPunchClassifier.classify(acceleration)
            leftStrength = acceleration.magnitude()
        } else {
            right = rightPunchClassifier.classify(acceleration)
            rightStrength = acceleration.magnitude()
        }

    }

    override fun onConnect() {
        setChanged()
        notifyObservers()
    }

    override fun onDisconnect() {
        setChanged()
        notifyObservers()
    }


}