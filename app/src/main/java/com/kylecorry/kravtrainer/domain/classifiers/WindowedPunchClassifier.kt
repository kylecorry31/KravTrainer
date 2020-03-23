package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType

abstract class WindowedPunchClassifier(private val windowSize: Int): IPunchClassifier {

    protected val window = mutableListOf<Acceleration>()

    override fun classify(reading: Acceleration): PunchType? {
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