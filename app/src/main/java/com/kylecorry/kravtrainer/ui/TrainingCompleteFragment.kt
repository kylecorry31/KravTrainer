package com.kylecorry.kravtrainer.ui

import android.hardware.SensorManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.domain.training.TrainingSession
import kotlin.math.roundToInt


class TrainingCompleteFragment(private val session: TrainingSession) : Fragment(),
    TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_complete, container, false)

        val numPunchesTxt = view.findViewById<TextView>(R.id.num_punches)
        val numCombosTxt = view.findViewById<TextView>(R.id.num_combos)
        val accuracyTxt = view.findViewById<TextView>(R.id.accuracy)
        val minutesTxt = view.findViewById<TextView>(R.id.minutes)
        val punchSpeedTxt = view.findViewById<TextView>(R.id.punches_per_second)
        val strengthTxt = view.findViewById<TextView>(R.id.strength)
        val doneBtn = view.findViewById<Button>(R.id.home_btn)

        numPunchesTxt.text = session.punches.toString()
        numCombosTxt.text = session.combos.toString()
        accuracyTxt.text = "${(session.accuracy * 100).roundToInt()} %"
        minutesTxt.text = session.duration.toFormattedString()
        punchSpeedTxt.text = String.format("%.1f", session.punchesPerSecond)
        strengthTxt.text = "${(session.strength / SensorManager.GRAVITY_EARTH).roundToInt()}g"

        doneBtn.setOnClickListener {
            fragmentManager?.doTransaction {
                this.replace(
                    R.id.fragment_holder,
                    TrainingSelectFragment()
                )
            }
        }

        tts = TextToSpeech(context, this)

        return view
    }

    override fun onPause() {
        super.onPause()
        if (tts.isSpeaking){
            tts.stop()
        }
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            tts.speak("Training complete, nice work!", TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}
