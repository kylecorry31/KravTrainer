package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class DTWPunchClassifier: IPunchClassifier {

    val windowSize = 15

    val window = mutableListOf<Acceleration>()

    val moves = listOf(
//        Pair(PunchType.Uppercut, transformAccelerations(readCSV("6.71,-0.32,6\n5.48,-0.16,4.61\n6.49,-1.54,3.67\n5.7,-1.38,1.94\n9.69,-3.39,0.34\n28.35,-2.86,-7.53\n48.09,-49.62,17.94\n14.32,-39.48,38.81\n-9.64,0.7,11.67\n-21.74,-13.92,19.83\n-4.8,12.06,-0.9\n5.34,-0.23,0.36\n8.96,1.34,1.03\n8.5,3.55,-1.7\n8.19,-0.09,-2.22"))),
        Pair(PunchType.Straight, transformAccelerations(readCSV("8.64,-4.75,0.54\n8.15,-5.72,0.51\n9.08,-4.56,0.82\n7.94,-7.35,1.69\n9.7,-6.58,2.04\n13.09,-12.49,-1.27\n45.38,20.65,-41.27\n-6.96,10.78,-54.72\n-98.73,73.89,82.04\n-18.13,23.35,13.51\n-11.4,21.29,-2.53\n4.89,4.63,-8.24\n16.99,-3.09,-14.36\n20.79,4.71,-15.41\n8.79,-6.81,-3.12"))),
        Pair(PunchType.Hook, transformAccelerations(readCSV("8.19,-0.39,2.86\n6.86,1.25,4.16\n8.13,2.77,5.92\n13.62,1.78,5.28\n33.61,-0.68,-4.99\n3.05,11.29,-86.92\n-37.07,21.51,-114.6\n-68.12,2.68,22.05\n-36.97,15.9,49.31\n-22.41,-1.05,22.72\n-4.59,-8.11,-2.33\n3.96,-13.91,-15.51\n6.64,-8.29,-17.96\n8.91,-10.33,-8.72\n10.95,-2.44,-3.05")))
    )

    private var lastReading = 0L
    private var lastMove: PunchType? = null

    override fun classify(reading: Acceleration): PunchType? {

        window.add(reading)

        if (window.size > windowSize){
            window.removeAt(0)
        }

        if (window.size < windowSize){
            return null
        }

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastReading < 200L){
            return lastMove
        }

        var minDtw = 1000000f
        var minMove: PunchType? = null

        val transformedWindow = transformAccelerations(window)

        for (move in moves){
            val moveDtw = dtw(move.second, transformedWindow)
            if (moveDtw < minDtw){
                minDtw = moveDtw
                minMove = move.first
            }
        }

        if (minDtw < 23000) {
            lastReading = currentTime
            lastMove = minMove
            return minMove
        }

        return null
    }

    private fun transformAccelerations(readings: List<Acceleration>): List<Acceleration> {
        val xs = (detrend(readings.map { it.x }))
        val ys = (detrend(readings.map { it.y }))
        val zs = (detrend(readings.map { it.z }))

        val newReadings = mutableListOf<Acceleration>()

        for (i in readings.indices){
            newReadings.add(Acceleration(xs[i], ys[i], zs[i]))
        }

        return newReadings
    }

    private fun demean(data: List<Float>): List<Float> {
        val mean = data.average().toFloat()
        return data.map { (it - mean) }
    }

    private fun scale(data: List<Float>): List<Float> {
        val mean = data.average().toFloat()

        var variance = 0f

        for (point in data){
            variance += (point - mean).pow(2)
        }

        val stdev = sqrt(variance / data.size)

        return data.map { (it - mean) / stdev }
    }

    private fun detrend(data: List<Float>): List<Float> {
        val xBar = ((data.size - 1) * data.size / 2) / data.size
        val yBar = data.average().toFloat()

        var ssxx = 0.0f
        var ssxy = 0.0f
        var ssto = 0.0f

        for (i in data.indices) {
            ssxx += (i - xBar).toFloat().pow(2)
            ssxy += (i - xBar) * (data[i] - yBar)
            ssto += (data[i] - yBar).pow(2)
        }

        val b1 = ssxy / ssxx
        val b0 = yBar - xBar * b1


        return data.mapIndexed { i, d -> d - (b0 + b1 * i) }
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

    private fun readCSV(csv: String): List<Acceleration> {
        return csv.split("\n").map { line -> line.split(",").map { it.toFloat() } }.map { Acceleration(it[0], it[1], it[2]) }
    }

}