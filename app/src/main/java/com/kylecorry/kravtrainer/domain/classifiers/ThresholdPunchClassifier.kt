package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType

class ThresholdPunchClassifier: IPunchClassifier {

    private val antiDuplication = AntiDuplicationFilter(400L)
    private val hookThreshold = 35
    private val straightThreshold = 25
    private val initialThreshold = 15

    override fun classify(reading: Acceleration): PunchType? {

        if ((reading.z < -hookThreshold || reading.y < -hookThreshold) && reading.x < initialThreshold){
            return antiDuplication.filter(PunchType.Hook)
        }

        if (reading.x < -straightThreshold){
            return antiDuplication.filter(PunchType.Straight)
        }

        return antiDuplication.filter(null)
    }

}