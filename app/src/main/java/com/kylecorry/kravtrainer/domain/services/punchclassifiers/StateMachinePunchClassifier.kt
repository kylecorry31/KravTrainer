package com.kylecorry.kravtrainer.domain.services.punchclassifiers

import com.kylecorry.kravtrainer.domain.models.Acceleration
import com.kylecorry.kravtrainer.domain.models.PunchType

class StateMachinePunchClassifier :
    IPunchClassifier {

    private val punches = mapOf(
        Pair(PunchType.Straight,
            StraightPunchSM()
        ),
        Pair(PunchType.Hook,
            HookSM()
        ),
        Pair(PunchType.Uppercut,
            UppercutSM()
        ),
        Pair(PunchType.Liver,
            LiverSM()
        ),
        Pair(PunchType.Hammer,
            HammerSM()
        )
    )

    private var lastReading = 0L

    override fun classify(reading: Acceleration): PunchType? {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastReading < 200L){
            return null
        }

        for (punch in punches){
            if (punch.value.update(reading)){
                punches.forEach { it.value.reset() }
                lastReading = currentTime
                return punch.key
            }
        }

        return null
    }

    private interface IPunchStateMachine {

        fun update(acceleration: Acceleration): Boolean

        fun reset()

    }


    private class StraightPunchSM:
        IPunchStateMachine {

        private var state = 0

        private var finalState = 2

        override fun update(acceleration: Acceleration): Boolean {
            when (state){
                0 -> {
                    if (acceleration.z < -15 && acceleration.x > 15){
                        state++
                    }
                }
                1 -> {
                    if (acceleration.x < -30){
                        state++
                    }
                }
                finalState -> {
                    reset()
                }
            }

            return state == finalState
        }

        override fun reset() {
            state = 0
        }

    }

    private class HookSM:
        IPunchStateMachine {

        private var state = 0

        private var finalState = 2

        override fun update(acceleration: Acceleration): Boolean {
            when (state){
                0 -> {
                    if (acceleration.x > 15){
                        state++
                    }
                }
                1 -> {
                    if (acceleration.z < -40 || acceleration.y < -40){
                        state++
                    }
                }
                2 -> {
                    if (acceleration.x < -30){
                        state++
                    }
                }
                finalState -> {
                    reset()
                }
            }

            return state == finalState
        }

        override fun reset() {
            state = 0
        }

    }

    private class UppercutSM:
        IPunchStateMachine {

        private var state = 0

        private var finalState = 1

        override fun update(acceleration: Acceleration): Boolean {
            when (state) {
                0 -> {
                }
                finalState -> {
                    reset()
                }
            }

            return state == finalState
        }

        override fun reset() {
            state = 0
        }

    }

    private class LiverSM:
        IPunchStateMachine {

        private var state = 0

        private var finalState = 1

        override fun update(acceleration: Acceleration): Boolean {
            when (state){
                0 -> {
                }
                finalState -> {
                    reset()
                }
            }

            return state == finalState
        }

        override fun reset() {
            state = 0
        }

    }

    private class HammerSM:
        IPunchStateMachine {

        private var state = 0

        private var finalState = 1

        override fun update(acceleration: Acceleration): Boolean {
            when (state){
                0 -> {
                }
                finalState -> {
                    reset()
                }
            }

            return state == finalState
        }

        override fun reset() {
            state = 0
        }

    }
}

