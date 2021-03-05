package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.PunchType
import com.kylecorry.trailsensecore.domain.math.Vector3

abstract class WindowedPunchClassifier(private val windowSize: Int): IPunchClassifier {

    protected val window = mutableListOf<Vector3>()

    override fun classify(reading: Vector3): PunchType? {
        window.add(reading)

        if (window.size > windowSize){
            window.removeAt(0)
        }

        return null
    }

    protected fun isWindowFull(): Boolean {
        return window.size == windowSize
    }
}