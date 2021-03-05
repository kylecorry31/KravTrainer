package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.trailsensecore.domain.math.Vector3

interface IPunchClassifier {

    fun classify(reading: Vector3): PunchType?

}