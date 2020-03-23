package com.kylecorry.kravtrainer.domain.punches

data class Punch(val hand: Hand, val punchType: PunchType){
    companion object {
        fun left(punchType: PunchType): Punch {
            return Punch(Hand.Left, punchType)
        }

        fun right(punchType: PunchType): Punch {
            return Punch(Hand.Right, punchType)
        }
    }
}