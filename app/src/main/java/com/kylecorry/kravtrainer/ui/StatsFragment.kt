package com.kylecorry.kravtrainer.ui

import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.domain.models.TrainingStats
import com.kylecorry.kravtrainer.infrastructure.StatsDB
import kotlin.math.max
import kotlin.math.roundToInt

class StatsFragment : Fragment() {

    private lateinit var numPunchesTxt: TextView
    private lateinit var numCombosTxt: TextView
    private lateinit var accuracyTxt: TextView
    private lateinit var minutesTxt: TextView
    private lateinit var strengthTxt: TextView

    private lateinit var db: StatsDB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        numPunchesTxt = view.findViewById(R.id.num_punches)
        numCombosTxt = view.findViewById(R.id.num_combos)
        accuracyTxt = view.findViewById(R.id.accuracy)
        minutesTxt = view.findViewById(R.id.minutes)
        strengthTxt = view.findViewById(R.id.strength)

        return view
    }

    override fun onResume() {
        super.onResume()
        db = StatsDB(context!!)
        updateUI(db.stats)
    }

    private fun updateUI(stats: List<TrainingStats>){
        var incorrect = 0
        var correct = 0
        var combos = 0
        var duration = 0
        var force = 0f

        for (stat in stats){
            incorrect += stat.incorrect
            correct += stat.correct
            combos += stat.combos
            duration += stat.seconds
            force = max(force, stat.strength)
        }

        force /= SensorManager.GRAVITY_EARTH

        val punches = incorrect + correct
        var accuracy = 0f
        if (punches > 0){
            accuracy = correct / punches.toFloat()
        }

        numPunchesTxt.text = punches.toString()
        numCombosTxt.text = combos.toString()
        accuracyTxt.text = "${(accuracy * 100).roundToInt()} %"
        minutesTxt.text = "${(duration / 60)}:${(duration % 60).toString().padStart(2, '0')}"
        strengthTxt.text = "${force.roundToInt()}g"

    }
}
