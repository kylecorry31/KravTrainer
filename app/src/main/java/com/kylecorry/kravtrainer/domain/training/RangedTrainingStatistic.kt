package com.kylecorry.kravtrainer.domain.training

import java.time.LocalDate

data class RangedTrainingStatistic(val startDate: LocalDate, val data: List<Number>)