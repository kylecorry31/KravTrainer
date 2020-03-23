package com.kylecorry.kravtrainer.domain.classifiers

import com.kylecorry.kravtrainer.domain.punches.Acceleration
import com.kylecorry.kravtrainer.domain.punches.PunchType
import java.lang.StringBuilder

class OrderPunchClassifier(private val threshold: Float): WindowedPunchClassifier(15) {

    private val punchSequences = mapOf(
        Pair(PunchType.Straight, listOf("Xx")), // , "Xzx", "XzYx", "XYzx"
        Pair(PunchType.Hook, listOf("Xy", "Xz"))//,"Xzy", "XYzy" /**Uppercut**/"yzxZ", "ZXy")) // XzxyZy, XzxyZ
    )

    override fun classify(reading: Acceleration): PunchType? {
        super.classify(reading)

        if (!isWindowFull()){
            return null
        }

        val sequence = getSequence(window)

        for (punch in punchSequences){
            for (seq in punch.value){
                if (sequence.startsWith(seq)){
                    return punch.key
                }
            }
        }

        return null
    }


    private fun getSequence(readings: List<Acceleration>): String {

        var lastX = readings[0].x
        var lastY = readings[0].y
        var lastZ = readings[0].z

        val sb = StringBuilder()

        for (i in 1 until readings.size){

            if (readings[i].x >= threshold && lastX < threshold){
                sb.append('X')
            }

            if (readings[i].x <= -threshold && lastX > -threshold){
                sb.append('x')
            }

            if (readings[i].y >= threshold && lastY < threshold){
                sb.append('Y')
            }

            if (readings[i].y <= -threshold && lastY > -threshold){
                sb.append('y')
            }

            if (readings[i].z >= threshold && lastZ < threshold){
                sb.append('Z')
            }

            if (readings[i].z <= -threshold && lastZ > -threshold){
                sb.append('z')
            }

            lastX = readings[i].x
            lastY = readings[i].y
            lastZ = readings[i].z
        }

        return sb.toString()
    }


}