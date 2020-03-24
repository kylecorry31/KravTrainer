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

    val punchesPerSecond: Float
        get(){
            val seconds = duration.seconds
            if (seconds == 0L){
                return 0f
            }

            return punches / seconds.toFloat()
        }

    fun calories(weightKg: Float): Float {
        // Formula from https://captaincalculator.com/health/calorie/calories-burned-boxing-calculator/
        val met = 5f
        val calPerMin = (met * weightKg * 3.5f) / 200f
        val minutes = duration.seconds / 60f
        return calPerMin * minutes
    }
}