package com.kylecorry.kravtrainer.domain.training

import java.lang.IllegalStateException
import java.time.Duration
import java.time.LocalDateTime

class TrainingSessionRecorder {

    private var correctPunches = 0
    private var incorrectPunches = 0
    private var combos: Int = 0
    private var strength: Float = 0f

    private var startTime: LocalDateTime? = null

    fun start(){
        startTime = LocalDateTime.now()
    }

    fun correct(){
        correctPunches++
    }

    fun incorrect(){
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

    fun createSessionReport(): TrainingSession {
        if (startTime == null){
            throw IllegalStateException("Session must be started before creating a report")
        }
        return TrainingSession(
            -1,
            startTime!!,
            Duration.between(startTime, LocalDateTime.now()),
            incorrectPunches,
            correctPunches,
            strength,
            combos
        )
    }

}