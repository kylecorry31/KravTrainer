package com.kylecorry.kravtrainer

object PunchCombos {

    val combos = listOf(
        PunchCombo("1", listOf(Punch.Left(PunchType.Straight))),
        PunchCombo("2", listOf(Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight))),
        PunchCombo("3", listOf(Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight), Punch.Left(PunchType.Straight))),
        PunchCombo("4", listOf(Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight), Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight))),
        PunchCombo("5", listOf(Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight), Punch.Left(PunchType.Straight), Punch.Left(PunchType.Straight), Punch.Right(PunchType.Straight)))
    )

}