package com.kylecorry.kravtrainer.domain.services

object PunchClassifierFactory {

    fun createPunchClassifier(): IPunchClassifier {
        return RuleBasedPunchClassifier()
    }

}