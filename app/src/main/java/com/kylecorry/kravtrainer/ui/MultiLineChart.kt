package com.kylecorry.kravtrainer.ui

import androidx.annotation.ColorInt
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import com.github.mikephil.charting.data.Entry;


class MultiLineChart(private val chart: LineChart) {

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

    fun plot(data: List<Dataset>) {
        val sets = data.mapIndexed { index, dataset ->
            val values = dataset.data.map { Entry(it.first.toFloat(), it.second.toFloat()) }

            val set1 = LineDataSet(values, "Series $index")
            set1.color = dataset.color
            set1.fillAlpha = 180
            set1.lineWidth = 3f
            set1.setDrawValues(false)
            set1.fillColor = dataset.color
            set1.setCircleColor(dataset.color)
            set1.setDrawCircleHole(false)
            set1.setDrawCircles(true)
            set1.circleRadius = 1.5f
            set1.setDrawFilled(false)
            set1
        }


        val lineData = LineData(sets)
        chart.data = lineData
        chart.legend.isEnabled = false
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    data class Dataset(val data: List<Pair<Number, Number>>, @ColorInt val color: Int)
}