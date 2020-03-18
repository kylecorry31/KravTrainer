package com.kylecorry.kravtrainer

data class Punch(val hand: Hand, val punchType: PunchType){
    companion object {
        @JvmStatic
        fun Left(punchType: PunchType): Punch {
            return Punch(Hand.Left, punchType)
        }

        @JvmStatic
        fun Right(punchType: PunchType): Punch {
            return Punch(Hand.Right, punchType)
        }
    }
}