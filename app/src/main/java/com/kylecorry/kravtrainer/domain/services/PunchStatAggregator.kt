package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.Punch
import com.kylecorry.kravtrainer.domain.models.TrainingStats
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

    fun recordStrength(acceleration: Acceleration){
        val magnitude = sqrt(acceleration.x.pow(2) + acceleration.y.pow(2) + acceleration.z.pow(2))

        if (magnitude > strength){
            strength = magnitude
        }
    }

    fun getStats(seconds: Int): TrainingStats {
        return TrainingStats(correctPunches, incorrectPunches, combos, strength, seconds)
    }

}