package com.kylecorry.kravtrainer.domain.combos

import com.kylecorry.kravtrainer.domain.punches.Punch
import com.kylecorry.kravtrainer.domain.punches.PunchType

object PunchCombos {

    val combos = listOf(
        PunchCombo(
            "1",
            listOf(
                Punch.left(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "2",
            listOf(
                Punch.left(
                    PunchType.Straight
                ), Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "3",
            listOf(
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                ),
                Punch.left(
                    PunchType.Hook
                )
            )
        ),
        PunchCombo(
            "4",
            listOf(
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                ),
                Punch.left(
                    PunchType.Hook
                ),
                Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "5",
            listOf(
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                ),
                Punch.left(
                    PunchType.Liver
                ),
                Punch.left(
                    PunchType.Hook
                ),
                Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "Jab",
            listOf(
                Punch.left(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "Double jab cross",
            listOf(
                Punch.left(
                    PunchType.Straight
                ),
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "Left hook",
            listOf(
                Punch.left(
                    PunchType.Hook
                )
            )
        ),
        PunchCombo(
            "Right hook",
            listOf(
                Punch.right(
                    PunchType.Hook
                )
            )
        ),
        PunchCombo(
            "4 straight",
            listOf(
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                ),
                Punch.left(
                    PunchType.Straight
                ),
                Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "Cross",
            listOf(
                Punch.right(
                    PunchType.Straight
                )
            )
        ),
        PunchCombo(
            "Left Uppercut",
            listOf(
                Punch.left(
                    PunchType.Uppercut
                )
            )
        ),
        PunchCombo(
            "Right Uppercut",
            listOf(
                Punch.right(
                    PunchType.Uppercut
                )
            )
        )
    )

}