package com.kylecorry.kravtrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.databinding.FragmentGraphBinding
import com.kylecorry.kravtrainer.domain.classifiers.PunchClassifierFactory
import com.kylecorry.kravtrainer.infrastructure.gloves.BluetoothAccelerometer
import com.kylecorry.trailsensecore.domain.math.Vector3
import com.kylecorry.trailsensecore.infrastructure.sensors.asLiveData
import com.kylecorry.trailsensecore.infrastructure.sensors.bluetooth.BluetoothService
import com.kylecorry.trailsensecore.infrastructure.system.UiUtils

class AccelerationFragment: BoundFragment<FragmentGraphBinding>() {

    private val rightChart by lazy { MultiLineChart(binding.rightChart) }
    private val leftChart by lazy { MultiLineChart(binding.leftChart) }
    private val rightHistory = mutableListOf<Vector3>()
    private val leftHistory = mutableListOf<Vector3>()
    private val historyLength = 100

    private val bluetoothService by lazy { BluetoothService(requireContext()) }

    private val leftAddress by lazy { bluetoothService.devices.firstOrNull { it.name == "SHADOW-BOXER-LEFT" }?.address ?: "" }
    private val left by lazy { BluetoothAccelerometer(requireContext(), leftAddress) }
    private val leftClassifier by lazy { PunchClassifierFactory.createPunchClassifier() }

    private val rightAddress by lazy { bluetoothService.devices.firstOrNull { it.name == "SHADOW-BOXER-RIGHT" }?.address ?: "" }
    private val right by lazy { BluetoothAccelerometer(requireContext(), rightAddress) }
    private val rightClassifier by lazy { PunchClassifierFactory.createPunchClassifier() }


    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGraphBinding {
        return FragmentGraphBinding.inflate(layoutInflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        right.asLiveData().observe(viewLifecycleOwner, { onRight() })
        left.asLiveData().observe(viewLifecycleOwner, { onLeft() })
    }

    private fun onRight(){
        rightHistory.add(right.acceleration)
        while (rightHistory.size > historyLength){
            rightHistory.removeAt(0)
        }

        val x = rightHistory.mapIndexed { index, value -> index to value.x }
        val y = rightHistory.mapIndexed { index, value -> index to value.y }
        val z = rightHistory.mapIndexed { index, value -> index to value.z }

        val datasets = listOf(
            MultiLineChart.Dataset(x, UiUtils.color(requireContext(), R.color.colorPrimary)),
            MultiLineChart.Dataset(y, UiUtils.color(requireContext(), R.color.colorPrimaryDark)),
            MultiLineChart.Dataset(z, UiUtils.color(requireContext(), R.color.colorAccent)),
        )

        rightChart.plot(datasets)

        val classification = rightClassifier.classify(right.acceleration)
        if (classification != null) {
            binding.rightMove.text = "Right: $classification"
        }
    }

    private fun onLeft(){
        leftHistory.add(left.acceleration)
        while (leftHistory.size > historyLength){
            leftHistory.removeAt(0)
        }

        val x = leftHistory.mapIndexed { index, value -> index to value.x }
        val y = leftHistory.mapIndexed { index, value -> index to value.y }
        val z = leftHistory.mapIndexed { index, value -> index to value.z }

        val datasets = listOf(
            MultiLineChart.Dataset(x, UiUtils.color(requireContext(), R.color.colorPrimary)),
            MultiLineChart.Dataset(y, UiUtils.color(requireContext(), R.color.colorPrimaryDark)),
            MultiLineChart.Dataset(z, UiUtils.color(requireContext(), R.color.colorAccent)),
        )

        leftChart.plot(datasets)

        val classification = leftClassifier.classify(left.acceleration)
        if (classification != null) {
            binding.leftMove.text = "Left: $classification"
        }
    }

}