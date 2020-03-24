package com.kylecorry.kravtrainer.ui

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.time.*

inline fun FragmentManager.doTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun Duration.toFormattedString(): String {
    val hours = toHours()
    val minutes = toMinutes() % 60
    val secs = seconds % 60
    return String.format("%d:%02d:%02d", hours, minutes, secs)
}

fun LocalDateTime.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofLocal(this, ZoneId.systemDefault(), null)
}