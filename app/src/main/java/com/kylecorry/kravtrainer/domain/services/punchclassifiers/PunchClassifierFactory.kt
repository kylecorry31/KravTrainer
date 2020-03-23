package com.kylecorry.kravtrainer.domain.services.punchclassifiers

object PunchClassifierFactory {

    fun createPunchClassifier(): IPunchClassifier {
        val basePunchClassifier = RuleBasedPunchClassifier()
//        val basePunchClassifier = ThresholdSequencePunchClassifier(20f)
        return TemporalFilterPunchClassifier(basePunchClassifier, 200)
    }

}