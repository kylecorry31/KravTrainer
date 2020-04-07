package com.kylecorry.kravtrainer.ui

import java.time.Duration

object DurationFormatter {

    fun format(duration: Duration): String {

        if (duration >= Duration.ofDays(1)) {
            val days = duration.toDays()
            return "$days day${if (days == 1L) "" else "s"}"
        }

        if (duration >= Duration.ofHours(1)) {
            val hours = duration.toHours()
            return "$hours hour${if (hours == 1L) "" else "s"}"
        }

        var minutes = duration.toMinutes()
        if (duration.seconds % 60 > 30) {
            minutes++
        }
        return "$minutes min${if (minutes == 1L) "" else "s"}"
    }

}