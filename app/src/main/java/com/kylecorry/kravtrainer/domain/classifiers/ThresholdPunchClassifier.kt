package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType

class ThresholdPunchClassifier: IPunchClassifier {

    private val antiDuplication = AntiDuplicationFilter(200L)

    override fun classify(reading: Acceleration): PunchType? {

        if (reading.z < -40 || reading.y < -40){
            return antiDuplication.filter(PunchType.Hook)
        }

        if (reading.x < -25){
            return antiDuplication.filter(PunchType.Straight)
        }

        return antiDuplication.filter(null)
    }

}