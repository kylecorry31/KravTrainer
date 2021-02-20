package com.kylecorry.kravtrainer.infrastructure.gloves

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.kravtrainer.domain.classifiers.PunchClassifierFactory
import com.kylecorry.trailsensecore.infrastructure.sensors.AbstractSensor
import java.time.Instant

class BluetoothGloves(address: String): AbstractSensor() {
    private val bluetoothSensor by lazy { BluetoothService().getBluetoothSensor(address, 1) }
    private val leftPunchClassifier = PunchClassifierFactory.createPunchClassifier()
    private val rightPunchClassifier = PunchClassifierFactory.createPunchClassifier()

    var left: PunchType? = null
    var right: PunchType? = null
    var leftStrength: Float = 0f
    var rightStrength: Float = 0f

    private var lastMessageTime = Instant.MIN

    val isConnected: Boolean
        get() = bluetoothSensor?.isConnected == true

    override val hasValidReading: Boolean
        get() = bluetoothSensor?.hasValidReading == true

    override fun startImpl() {
        bluetoothSensor?.start(this::onBluetooth)
    }

    override fun stopImpl() {
        bluetoothSensor?.stop(this::onBluetooth)
    }

    private fun onBluetooth(): Boolean {
        bluetoothSensor?.let {
            val lastMessage = it.messages.lastOrNull()
            if (lastMessage != null && lastMessage.timestamp > lastMessageTime){
                lastMessageTime = lastMessage.timestamp
                onData(lastMessage.message)
            }
        }
        return true
    }


    private fun onData(data: String) {
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

        notifyListeners()
    }
}