package com.kylecorry.kravtrainer.infrastructure.gloves

import android.content.Context
import com.kylecorry.trailsensecore.domain.math.Vector3
import com.kylecorry.trailsensecore.domain.math.toFloatCompat
import com.kylecorry.trailsensecore.infrastructure.sensors.AbstractSensor
import com.kylecorry.trailsensecore.infrastructure.sensors.accelerometer.IAccelerometer
import com.kylecorry.trailsensecore.infrastructure.sensors.bluetooth.BluetoothSensor

class BluetoothAccelerometer(private val context: Context, private val address: String): AbstractSensor(), IAccelerometer {

    private val bluetoothSensor by lazy { BluetoothSensor(context, address, 1) }

    val isConnected: Boolean
        get() = bluetoothSensor.isConnected

    override val acceleration: Vector3
        get(){
            val lastMessage = bluetoothSensor.messages.lastOrNull() ?: return Vector3.zero
            val values = lastMessage.message.split(",").mapNotNull {
                it.toFloatCompat()
            }

            if (values.size != 3){
                return Vector3.zero
            }

            return Vector3(values[0], values[1], values[2])
        }
    override val hasValidReading: Boolean
        get() = bluetoothSensor.hasValidReading

    override fun startImpl() {
        bluetoothSensor.start(this::onBluetoothUpdate)
    }

    override fun stopImpl() {
        bluetoothSensor.stop(this::onBluetoothUpdate)
    }

    private fun onBluetoothUpdate(): Boolean {
        notifyListeners()
        return true
    }
}