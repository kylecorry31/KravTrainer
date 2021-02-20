package com.kylecorry.kravtrainer.infrastructure.gloves

interface IBluetoothSensor {
    val messages: List<BluetoothMessage>
    val isConnected: Boolean
    fun write(data: String): Boolean
}