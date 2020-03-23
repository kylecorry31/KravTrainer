package com.kylecorry.kravtrainer.infrastructure.gloves

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

object Bluetooth {

    private val adapter = BluetoothAdapter.getDefaultAdapter()

    val isEnabled: Boolean
        get() = adapter.isEnabled

    val devices: List<BluetoothDevice>
        get() = adapter.bondedDevices.toList()

    fun getDevice(address: String): BluetoothDevice {
        return adapter.getRemoteDevice(address)
    }

}