package com.kylecorry.kravtrainer.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.trailsensecore.infrastructure.sensors.bluetooth.BluetoothService
import com.kylecorry.trailsensecore.infrastructure.system.UiUtils


class TrainingSelectFragment : Fragment() {

    private lateinit var twoMinBtn: Button
    private lateinit var fourMinBtn: Button
    private lateinit var unlimitedBtn: Button

    private var leftAddress = ""
    private var rightAddress = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_training_select, container, false)
        twoMinBtn = view.findViewById(R.id.button_2_minutes)
        fourMinBtn = view.findViewById(R.id.button_4_minutes)
        val tenMinBtn = view.findViewById<Button>(R.id.button_10_minutes)
        unlimitedBtn = view.findViewById(R.id.button_unlimited)

        twoMinBtn.setOnClickListener {
            startTraining(60 * 2, leftAddress, rightAddress)
        }

        fourMinBtn.setOnClickListener {
            startTraining(60 * 4, leftAddress, rightAddress)
        }

        tenMinBtn.setOnClickListener {
            startTraining(60 * 10, leftAddress, rightAddress)
        }

        unlimitedBtn.setOnClickListener {
            startTraining(null, leftAddress, rightAddress)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val bluetoothService = BluetoothService(requireContext())

        if (!bluetoothService.isEnabled) {
            Toast.makeText(
                context,
                "Bluetooth is disabled, please enable it to train.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val left = bluetoothService.devices.firstOrNull { it.name == "SHADOW-BOXER-LEFT" }?.address
        val right =
            bluetoothService.devices.firstOrNull { it.name == "SHADOW-BOXER-RIGHT" }?.address

        if (left == null || right == null) {
            UiUtils.alert(
                requireContext(),
                "Shadow boxer gloves not paired",
                "Set the gloves to pairing mode then connect to them from your Bluetooth settings. The pin is 1234.",
                "Open Settings"
            ) {
                val bluetoothIntent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                startActivity(bluetoothIntent)
            }
        } else {
            leftAddress = left
            rightAddress = right
        }

    }

    private fun startTraining(time: Int?, leftAddress: String, rightAddress: String) {
        fragmentManager?.doTransaction {
            this.replace(
                R.id.fragment_holder,
                TrainingFragment(time, leftAddress, rightAddress)
            )
        }
    }
}
