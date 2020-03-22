package com.kylecorry.kravtrainer.domain.services.punchclassifiers

object PunchClassifierFactory {

    fun createPunchClassifier(): IPunchClassifier {
        return RuleBasedPunchClassifier()
    }

}