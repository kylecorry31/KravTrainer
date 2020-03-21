package com.kylecorry.kravtrainer.domain.models

object PunchCombos {

    val combos = listOf(
        PunchCombo(
            "1",
            listOf(Punch.left(PunchType.Straight))
        ),
        PunchCombo(
            "2",
            listOf(Punch.left(PunchType.Straight), Punch.right(
                PunchType.Straight
            ))
        ),
        PunchCombo(
            "3",
            listOf(
                Punch.left(PunchType.Straight),
                Punch.right(PunchType.Straight),
                Punch.left(PunchType.Hook)
            )
        ),
        PunchCombo(
            "4",
            listOf(
                Punch.left(PunchType.Straight),
                Punch.right(PunchType.Straight),
                Punch.left(PunchType.Hook),
                Punch.right(PunchType.Straight)
            )
        ),
        PunchCombo(
            "5",
            listOf(
                Punch.left(PunchType.Straight),
                Punch.right(PunchType.Straight),
                Punch.left(PunchType.Hook), // Liver
                Punch.left(PunchType.Hook),
                Punch.right(PunchType.Straight)
            )
        ),
        PunchCombo(
            "Jab",
            listOf(
                Punch.left(PunchType.Straight)
            )
        ),
        PunchCombo(
            "Cross",
            listOf(
                Punch.right(PunchType.Straight)
            )
        )
    )

}