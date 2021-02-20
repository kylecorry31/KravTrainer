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
import com.kylecorry.kravtrainer.infrastructure.gloves.BluetoothService


class TrainingSelectFragment : Fragment() {

    private lateinit var twoMinBtn: Button
    private lateinit var fourMinBtn: Button
    private lateinit var unlimitedBtn: Button

    private var address = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_select, container, false)
        twoMinBtn = view.findViewById(R.id.button_2_minutes)
        fourMinBtn = view.findViewById(R.id.button_4_minutes)
        val tenMinBtn = view.findViewById<Button>(R.id.button_10_minutes)
        unlimitedBtn = view.findViewById(R.id.button_unlimited)

        twoMinBtn.setOnClickListener {
            startTraining(60 * 2, address)
        }

        fourMinBtn.setOnClickListener {
            startTraining(60 * 4, address)
        }

        tenMinBtn.setOnClickListener {
            startTraining(60 * 10, address)
        }

        unlimitedBtn.setOnClickListener {
            startTraining(null, address)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        if (!BluetoothService().isEnabled){
            Toast.makeText(context, "Bluetooth is disabled, please enable it to train.", Toast.LENGTH_LONG).show()
        }

        // TODO: Allow user to override this
        val hc05 = BluetoothService().devices.firstOrNull { it.name == "HC-05" }

        if (hc05 != null){
            address = hc05.address
        } else {
            val dialog = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("Shadow boxer gloves not paired")
                    setMessage("Set the gloves to pairing mode then connect to 'HC-05' from your Bluetooth settings. The pin is 1234.")
                    setPositiveButton("Open settings") { _, _ ->
                        val bluetoothIntent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                        startActivity(bluetoothIntent)
                    }
                }
                builder.create()
            }
            dialog?.show()
        }
    }

    private fun startTraining(time: Int?, address: String){
        fragmentManager?.doTransaction {
            this.replace(
                R.id.fragment_holder,
                TrainingFragment(time, address)
            )
        }
    }
}
