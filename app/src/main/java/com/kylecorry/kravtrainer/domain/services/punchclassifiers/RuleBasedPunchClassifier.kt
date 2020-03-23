package com.kylecorry.kravtrainer.domain.services.punchclassifiers

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

class RuleBasedPunchClassifier: IPunchClassifier {

    override fun classify(reading: Acceleration): PunchType? {

        if (reading.z < -40 || reading.y < -40){
            return PunchType.Hook
        }

        if (reading.x < -25){
            return PunchType.Straight
        }

        return null
    }

}