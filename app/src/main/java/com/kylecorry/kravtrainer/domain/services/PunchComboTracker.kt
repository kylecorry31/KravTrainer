package com.kylecorry.kravtrainer.domain.services

import com.kylecorry.kravtrainer.domain.models.Punch
import com.kylecorry.kravtrainer.domain.models.PunchCombo

class PunchComboTracker(val combo: PunchCombo){
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
        return combo.punches[currentIdx] == punch
    }

    fun next() {
        if (isDone) return
        currentIdx++
    }

}