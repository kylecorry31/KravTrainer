package com.kylecorry.kravtrainer.domain.services.punchclassifiers

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

class TemporalFilterPunchClassifier(private val punchClassifier: IPunchClassifier, private val ignoreMilliseconds: Long): IPunchClassifier {

    private var lastTime = 0L

    override fun classify(reading: Acceleration): PunchType? {

        val punch = punchClassifier.classify(reading)

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime < ignoreMilliseconds){
            return null
        }

        if (punch != null){
            lastTime = currentTime
        }

        return punch
    }
}