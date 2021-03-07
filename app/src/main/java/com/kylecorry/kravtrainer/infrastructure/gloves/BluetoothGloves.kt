package com.kylecorry.kravtrainer.infrastructure.gloves

import android.content.Context
import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.trailsensecore.infrastructure.sensors.AbstractSensor

class BluetoothGloves(
    private val context: Context,
    private val leftAddress: String,
    private val rightAddress: String
) : AbstractSensor() {
    private val leftGlove by lazy { BluetoothGlove(context, leftAddress) }
    private val rightGlove by lazy { BluetoothGlove(context, rightAddress, true) }

    var left: PunchType? = null
    var right: PunchType? = null
    var leftStrength: Float = 0f
    var rightStrength: Float = 0f

    val isConnected: Boolean
        get() = leftGlove.isConnected && rightGlove.isConnected

    override val hasValidReading: Boolean
        get() = leftGlove.hasValidReading || rightGlove.hasValidReading

    override fun startImpl() {
        leftGlove.start(this::onLeft)
        rightGlove.start(this::onRight)
    }

    override fun stopImpl() {
        leftGlove.stop(this::onLeft)
        rightGlove.stop(this::onRight)
    }

    private fun onLeft(): Boolean {
        left = leftGlove.punch
        leftStrength = leftGlove.strength
        notifyListeners()
        return true
    }

    private fun onRight(): Boolean {
        right = rightGlove.punch
        rightStrength = rightGlove.strength
        notifyListeners()
        return true
    }
}