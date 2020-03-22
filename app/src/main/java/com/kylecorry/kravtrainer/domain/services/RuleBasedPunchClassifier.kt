package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

class RuleBasedPunchClassifier: IPunchClassifier {

    private var lastReading = 0L

    override fun classify(reading: Acceleration): PunchType? {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastReading < 200L){
            return null
        }

        if (reading.z < -40 || reading.y < -40){
            lastReading = currentTime
            return PunchType.Hook
        }

        if (reading.x < -25){
            lastReading = currentTime
            return PunchType.Straight
        }

        return null
    }

}