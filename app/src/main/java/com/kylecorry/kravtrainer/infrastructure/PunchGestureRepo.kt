package com.kylecorry.kravtrainer.infrastructure

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

object PunchGestureRepo {

    // TODO: Load from CSV
    var punches = mapOf<PunchType, List<Acceleration>>()
        private set

    fun load(){
        punches = mapOf(
            Pair(PunchType.Straight, listOf(Acceleration(1f, 2f, 3f)))
        )
    }

}