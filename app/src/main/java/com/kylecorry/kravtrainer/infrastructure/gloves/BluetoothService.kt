package com.kylecorry.kravtrainer.infrastructure.gloves

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothService {

    private val adapter by lazy { BluetoothAdapter.getDefaultAdapter() }

    val isEnabled: Boolean
        get() = adapter.isEnabled

    val devices: List<BluetoothDevice>
        get() = adapter.bondedDevices.toList()

    fun getDevice(address: String): BluetoothDevice? {
        return try {
            adapter.getRemoteDevice(address)
        } catch (e: Exception){
            null
        }
    }

}