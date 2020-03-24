package com.kylecorry.kravtrainer.ui

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import com.github.mikephil.charting.data.Entry;


class MpLineChart(private val chart: LineChart, private val color: Int): ILineChart {

    init {
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)

        chart.xAxis.setDrawLabels(false)
        chart.axisRight.setDrawLabels(false)

        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.xAxis.setDrawAxisLine(false)
        chart.axisLeft.setDrawAxisLine(false)
        chart.axisRight.setDrawAxisLine(false)
    }

    override fun plot(data: List<Pair<Number, Number>>) {
        val values = data.map { Entry(it.first.toFloat(), it.second.toFloat()) }

        val set1 = LineDataSet(values, "Series 1")
        set1.color = color
        set1.fillAlpha = 180
        set1.circleColors = listOf(color)
        set1.lineWidth = 2f
        set1.setDrawValues(false)
        set1.mode = LineDataSet.Mode.LINEAR
        set1.fillColor = color
        set1.setDrawCircleHole(false)
        set1.setDrawCircles(true)
        set1.setDrawFilled(false)

        val lineData = LineData(set1)
        chart.data = lineData
        chart.legend.isEnabled = false
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}