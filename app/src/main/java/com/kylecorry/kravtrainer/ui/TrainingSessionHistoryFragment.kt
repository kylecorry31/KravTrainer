package com.kylecorry.kravtrainer.ui

import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.domain.training.TrainingSession
import com.kylecorry.kravtrainer.infrastructure.traininghistory.TrainingSessionRepo
import java.time.Duration
import kotlin.math.max
import kotlin.math.roundToInt

class TrainingSessionHistoryFragment : Fragment() {

    private lateinit var numPunchesTxt: TextView
    private lateinit var numCombosTxt: TextView
    private lateinit var accuracyTxt: TextView
    private lateinit var minutesTxt: TextView
    private lateinit var strengthTxt: TextView

    private lateinit var sessionRepo: TrainingSessionRepo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_session_history, container, false)

        numPunchesTxt = view.findViewById(R.id.num_punches)
        numCombosTxt = view.findViewById(R.id.num_combos)
        accuracyTxt = view.findViewById(R.id.accuracy)
        minutesTxt = view.findViewById(R.id.minutes)
        strengthTxt = view.findViewById(R.id.strength)

        return view
    }

    override fun onResume() {
        super.onResume()
        sessionRepo =
            TrainingSessionRepo(
                context!!
            )
        updateUI(sessionRepo.getAll())
    }

    private fun updateUI(sessions: List<TrainingSession>){
        // TODO: List all sessions
        // TODO: Display graph of accuracy (or other info) over time
        var incorrect = 0
        var correct = 0
        var combos = 0
        var duration = Duration.ofSeconds(0)
        var force = 0f

        for (session in sessions){
            incorrect += session.incorrect
            correct += session.correct
            combos += session.combos
            duration = duration.plus(session.duration)
            force = max(force, session.strength)
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
        minutesTxt.text = duration.toFormattedString()
        strengthTxt.text = "${force.roundToInt()}g"

    }
}
