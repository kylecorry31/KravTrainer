package com.kylecorry.kravtrainer.domain.training

import java.time.Duration
import java.time.LocalDateTime

data class TrainingSession(
    val id: Int,
    val date: LocalDateTime,
    val duration: Duration,
    val incorrect: Int,
    val correct: Int,
    val strength: Float,
    val combos: Int
) {
    val punches: Int
        get() = correct + incorrect

    val accuracy: Float
        get(){
            if (punches == 0){
                return 0f
            }
            return correct / punches.toFloat()
        }
}