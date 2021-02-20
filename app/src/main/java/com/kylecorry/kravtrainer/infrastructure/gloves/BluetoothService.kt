package com.kylecorry.kravtrainer.infrastructure.gloves

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothService {

    private val adapter by lazy { BluetoothAdapter.getDefaultAdapter() }

    val isEnabled: Boolean
        get() = adapter.isEnabled

    val devices: List<BluetoothDevice>
        get() = adapter.bondedDevices.toList()

    private fun getDevice(address: String): BluetoothDevice? {
        return try {
            adapter.getRemoteDevice(address)
        } catch (e: Exception){
            null
        }
    }

    fun getBluetoothSensor(address: String, messageHistorySize: Int = 1): BluetoothSensor? {
        val device = getDevice(address) ?: return null
        return BluetoothSensor(device, messageHistorySize)
    }

}