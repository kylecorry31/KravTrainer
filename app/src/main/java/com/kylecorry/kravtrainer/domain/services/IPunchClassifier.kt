package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

interface IPunchClassifier {

    fun classify(reading: Acceleration): PunchType?

}