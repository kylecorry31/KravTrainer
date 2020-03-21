package com.kylecorry.kravtrainer.infrastructure

interface SerialListener {

    fun onData(data: String)

    fun onConnect()

    fun onDisconnect()

}