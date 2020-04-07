package com.kylecorry.kravtrainer.ui

import java.time.Duration
import java.time.LocalDateTime

object TimeAgoFormatter {

    fun format(dateTime: LocalDateTime): String {
        val duration = Duration.between(dateTime, LocalDateTime.now())

        if (duration >= Duration.ofDays(1)){
            val days = duration.toDays()
            return "$days day${if (days == 1L) "" else "s"} ago"
        }

        if (duration >= Duration.ofHours(1)){
            val hours = duration.toHours()
            return "$hours hour${if (hours == 1L) "" else "s"} ago"
        }

        if (duration < Duration.ofMinutes(1)){
            return "now"
        }

        val minutes = duration.toMinutes()
        return "$minutes min${if (minutes == 1L) "" else "s"} ago"
    }

}