package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType

interface IPunchClassifier {

    fun classify(reading: Acceleration): PunchType?

}