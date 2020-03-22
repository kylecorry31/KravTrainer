package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Punch
import com.kylecorry.kravtrainer.domain.models.TrainingSession
import java.time.Duration
import java.time.LocalDateTime

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

    fun getStats(duration: Duration): TrainingSession {
        return TrainingSession(
            -1,
            LocalDateTime.now(),
            duration,
            incorrectPunches,
            correctPunches,
            strength,
            combos
        )
    }

}