package com.kylecorry.kravtrainer.infrastructure.gloves

import android.content.Context
import com.kylecorry.kravtrainer.domain.classifiers.PunchClassifierFactory
import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.trailsensecore.infrastructure.sensors.AbstractSensor

class BluetoothGlove(private val context: Context, private val address: String, private val invertX: Boolean = false): AbstractSensor() {

    private val accelerometer by lazy { BluetoothAccelerometer(context, address) }
    private val punchClassifier = PunchClassifierFactory.createPunchClassifier()

    var punch: PunchType? = null
    var strength: Float = 0f

    val isConnected: Boolean
        get() = accelerometer.isConnected

    override val hasValidReading: Boolean
        get() = accelerometer.hasValidReading

    override fun startImpl() {
        accelerometer.start(this::onReading)
    }

    override fun stopImpl() {
        accelerometer.stop(this::onReading)
    }

    private fun onReading(): Boolean {
        val acceleration = accelerometer.acceleration
        punch = punchClassifier.classify(acceleration.copy(x = if (invertX) -acceleration.x else acceleration.x))
        strength = acceleration.magnitude()
        notifyListeners()
        return true
    }

}