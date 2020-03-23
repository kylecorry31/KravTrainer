package com.kylecorry.kravtrainer.domain.combos

import com.kylecorry.kravtrainer.domain.punches.Punch
import com.kylecorry.kravtrainer.domain.punches.PunchType

class ComboTracker(val combo: PunchCombo){
    private var currentIdx: Int = 0

    val index: Int
        get() = currentIdx

    val currentPunch: Punch?
        get(){
            if (isDone){
                return null
            }
            return combo.punches[currentIdx]
        }

    val isDone: Boolean
        get() = currentIdx >= combo.punches.size

    fun matches(punch: Punch): Boolean {
        if (isDone){
            return false
        }
        return matches(combo.punches[currentIdx], punch)
    }

    fun next() {
        if (isDone) return
        currentIdx++
    }

    private fun matches(current: Punch, punch: Punch): Boolean {
        if (current.hand != punch.hand){
            return false
        }

        if (current.punchType == punch.punchType){
            return true
        }

        if (current.punchType == PunchType.Uppercut && punch.punchType == PunchType.Hook){
            return true
        }

        if (current.punchType == PunchType.Liver && punch.punchType == PunchType.Hook){
            return true
        }

        return false
    }

}