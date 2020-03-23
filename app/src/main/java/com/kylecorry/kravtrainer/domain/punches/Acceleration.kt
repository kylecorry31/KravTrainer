package com.kylecorry.kravtrainer.domain.punches

import kotlin.math.pow
import kotlin.math.sqrt

data class Acceleration(val x: Float, val y: Float, val z: Float){

    fun magnitude(): Float {
        return sqrt(x.pow(2) + y.pow(2) + z.pow(2))
    }

}