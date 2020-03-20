package com.kylecorry.kravtrainer.domain.models

import kotlin.math.pow

data class Acceleration(val x: Float, val y: Float, val z: Float){

    /**
     * Calculates the (rough) distance to another acceleration
     */
    fun distanceTo(other: Acceleration): Float {
        return (x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2)
    }

    companion object {
        val zero = Acceleration(0f, 0f, 0f)
    }
}