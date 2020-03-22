package com.kylecorry.kravtrainer.domain.services

import java.util.*
import kotlin.concurrent.fixedRateTimer

class TrainingTimer(private val totalSeconds: Int): Observable() {

    var remainingSeconds: Int = totalSeconds
        private set(value) {
            setChanged()
            notifyObservers()
            field = value
        }

    private var timer: Timer? = null

    fun start(){
        remainingSeconds = totalSeconds
        timer = fixedRateTimer(period = 1000){
            if (remainingSeconds > 0){
                remainingSeconds--
            } else {
                stop()
            }
        }
    }

    fun stop(){
        remainingSeconds = 0
        timer?.cancel()
        timer = null
    }

}