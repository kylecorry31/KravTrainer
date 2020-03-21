package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.Constants
import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType
import com.kylecorry.kravtrainer.infrastructure.PunchGestureRepo
import kotlin.math.min

class PunchClassifier {

    private var lastReading = 0L

    private val detectionThreshold: Float = 10f

    private val readings = mutableListOf<Acceleration>()

    fun classify(reading: Acceleration): PunchType? {

//        readings.add(reading)
//
//        if (readings.size > Constants.PUNCH_WINDOW_LENGTH){
//            readings.removeAt(0)
//        }
//
//        for(punch in PunchGestureRepo.punches){
//            if (dtw(punch.value, readings) <= detectionThreshold){
//                return punch.key
//            }
//        }

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastReading < 200L){
            return null
        }

        if (reading.z < -45){
            lastReading = currentTime
            return PunchType.Hook
        }

        if (reading.x < -35){
            lastReading = currentTime
            return PunchType.Straight
        }

        // TODO: Return max dtw if greater than min threshold
        return null
    }

    private fun dtw(first: List<Acceleration>, second: List<Acceleration>): Float {

        val row = mutableListOf<Float>()

        for (acceleration in second){
            row.add(first.first().distanceTo(acceleration))
        }

        for (i in 1 until first.size){

            var temp = row.first()
            row[0] = first[i].distanceTo(second.first()) + row.first()

            for (j in 1 until second.size){
                val temp2 = row[j]

                row[j] = first[i].distanceTo(second[j]) + min(row[j - 1], min(temp, temp2))

                temp = temp2
            }
        }

        return row.last()
    }

}