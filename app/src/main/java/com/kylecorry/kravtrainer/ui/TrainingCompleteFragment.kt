package com.kylecorry.kravtrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.doTransaction
import com.kylecorry.kravtrainer.domain.models.*
import kotlin.math.roundToInt


class TrainingCompleteFragment(private val stats: TrainingStats) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_complete, container, false)

        val numPunchesTxt = view.findViewById<TextView>(R.id.num_punches)
        val numCombosTxt = view.findViewById<TextView>(R.id.num_combos)
        val accuracyTxt = view.findViewById<TextView>(R.id.accuracy)
        val minutesTxt = view.findViewById<TextView>(R.id.minutes)
        val strengthTxt = view.findViewById<TextView>(R.id.strength)
        val doneBtn = view.findViewById<Button>(R.id.home_btn)

        numPunchesTxt.text = stats.punches.toString()
        numCombosTxt.text = stats.combos.toString()
        accuracyTxt.text = "${(stats.accuracy * 100).roundToInt()} %"
        minutesTxt.text = "${(stats.seconds / 60)}:${(stats.seconds % 60).toString().padStart(2, '0')}"
        strengthTxt.text = "${stats.strength.roundToInt()}g"

        doneBtn.setOnClickListener {
            fragmentManager?.doTransaction {
                this.replace(
                    R.id.fragment_holder,
                    TrainingSelectFragment()
                )
            }
        }

        return view
    }
}
