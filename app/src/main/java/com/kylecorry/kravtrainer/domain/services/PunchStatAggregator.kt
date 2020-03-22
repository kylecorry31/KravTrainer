package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.Punch
import com.kylecorry.kravtrainer.domain.models.TrainingStats
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.sqrt

class PunchStatAggregator {

    private var correctPunches = 0

    private var incorrectPunches = 0

    private var combos: Int = 0

    private var strength: Float = 0f

    fun correct(punch: Punch){
        correctPunches++
    }

    fun incorrect(punch: Punch){
        incorrectPunches++
    }

    fun completeCombo(){
        combos++
    }

    fun recordStrength(strengthMeterSecondSqr: Float){
        if (strengthMeterSecondSqr > strength){
            strength = strengthMeterSecondSqr
        }
    }

    fun getStats(seconds: Int): TrainingStats {
        return TrainingStats(-1, LocalDateTime.now(), correctPunches, incorrectPunches, combos, strength, seconds)
    }

}