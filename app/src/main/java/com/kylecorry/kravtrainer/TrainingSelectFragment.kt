package com.kylecorry.kravtrainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class TrainingSelectFragment : Fragment() {

    private lateinit var twoMinBtn: Button
    private lateinit var fourMinBtn: Button
    private lateinit var unlimitedBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_select, container, false)
        twoMinBtn = view.findViewById(R.id.button_2_minutes)
        fourMinBtn = view.findViewById(R.id.button_4_minutes)
        unlimitedBtn = view.findViewById(R.id.button_unlimited)

        twoMinBtn.setOnClickListener {
            startTraining(1000 * 60 * 2)
        }

        fourMinBtn.setOnClickListener {
            startTraining(1000 * 60 * 4)
        }

        unlimitedBtn.setOnClickListener {
            startTraining(null)
        }
        return view
    }

    private fun startTraining(time: Long?){
        fragmentManager?.doTransaction {
            this.replace(R.id.fragment_holder,
                TrainingFragment(time)
            )
        }
    }
}
