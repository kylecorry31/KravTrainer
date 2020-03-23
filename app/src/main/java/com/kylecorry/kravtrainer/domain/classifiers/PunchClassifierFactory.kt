package com.kylecorry.kravtrainer.domain.classifiers

object PunchClassifierFactory {

    fun createPunchClassifier(): IPunchClassifier {
        return ThresholdPunchClassifier()
    }

}