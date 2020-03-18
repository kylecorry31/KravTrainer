package com.kylecorry.kravtrainer

class PunchComboTracker(val combo: PunchCombo){
    private var currentIdx: Int = 0

    val progress: Float
        get() = currentIdx / combo.punches.size.toFloat()

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