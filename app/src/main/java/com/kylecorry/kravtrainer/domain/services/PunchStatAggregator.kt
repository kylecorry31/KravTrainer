package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Punch

class PunchStatAggregator {

    var correctPunches = 0
        private set

    var incorrectPunches = 0
        private set

    val punches: Int
        get() = correctPunches + incorrectPunches


    fun correct(punch: Punch){
        correctPunches++
    }

    fun incorrect(punch: Punch){
        incorrectPunches++
    }

}