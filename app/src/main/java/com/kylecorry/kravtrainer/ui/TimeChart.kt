package com.kylecorry.kravtrainer.ui

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kylecorry.kravtrainer.domain.training.RangedTrainingStatistic


class TimeChart(private val chart: BarChart, private val color: Int) {

    init {
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)

        chart.xAxis.setDrawLabels(true)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.setDrawLabels(true)
        chart.axisLeft.setDrawLabels(false)

        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.gridColor = Color.valueOf(0f, 0f, 0f, 0.1f).toArgb()
        chart.axisRight.textColor = Color.valueOf(0f, 0f, 0f, 0.4f).toArgb()
        chart.axisRight.setDrawGridLines(true)
        chart.xAxis.setDrawAxisLine(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisRight.setDrawAxisLine(false)
        chart.axisRight.granularity = 5f
        val barChartRender = CustomBarChartRender(chart, chart.animator, chart.viewPortHandler)
        barChartRender.setRadius(20f)
        chart.renderer = barChartRender
    }

    fun plot(stats: RangedTrainingStatistic) {
        val values = stats.data.mapIndexed { index, number -> BarEntry(index.toFloat(), number.toFloat()) }
        chart.xAxis.labelCount = values.size

        val set1 = BarDataSet(values, "Series 1")
        set1.color = color
        chart.xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() == values.lastIndex){
                    "Today"
                } else {
                    ""
                }
            }
        }


        set1.setDrawValues(false)

        val lineData = BarData(set1)
        chart.data = lineData
        chart.legend.isEnabled = false
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}