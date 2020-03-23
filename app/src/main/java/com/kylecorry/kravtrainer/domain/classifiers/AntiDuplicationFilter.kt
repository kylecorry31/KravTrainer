package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.PunchType

class AntiDuplicationFilter(private val fastestIntervalMillis: Long) {
    private var lastTime = 0L

    fun filter(punch: PunchType?): PunchType? {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime < fastestIntervalMillis){
            return null
        }

        if (punch != null){
            lastTime = currentTime
        }

        return punch
    }
}