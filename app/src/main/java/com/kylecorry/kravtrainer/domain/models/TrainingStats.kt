package com.kylecorry.kravtrainer.domain.models

import java.time.LocalDateTime

data class TrainingStats(val id: Int, val date: LocalDateTime, val correct: Int, val incorrect: Int, val combos: Int, val strength: Float, val seconds: Int) {
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