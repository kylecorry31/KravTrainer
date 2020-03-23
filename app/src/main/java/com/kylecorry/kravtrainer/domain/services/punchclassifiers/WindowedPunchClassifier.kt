package com.kylecorry.kravtrainer.domain.services.punchclassifiers

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

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