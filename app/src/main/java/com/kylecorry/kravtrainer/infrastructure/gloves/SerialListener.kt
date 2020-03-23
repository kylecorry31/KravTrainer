package com.kylecorry.kravtrainer.infrastructure.gloves

interface SerialListener {

    fun onData(data: String)

    fun onConnect()

    fun onDisconnect()

}