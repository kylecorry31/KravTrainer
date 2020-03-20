package com.kylecorry.kravtrainer

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.roundToInt
import kotlin.random.Random

class TrainingFragment(private val time: Long?) : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var timeProgressBar: ProgressBar
    private lateinit var comboTxt: TextView
    private lateinit var endBtn: Button
    private lateinit var comboProgressDots: LinearLayout
    private lateinit var currentMoveTxt: TextView

    private lateinit var comboTracker: PunchComboTracker
    private lateinit var tts: TextToSpeech
    private lateinit var timer: Timer

    private var punches = 0
    private var correctPunches = 0
    private var timeRemaining = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training, container, false)

        timeProgressBar = view.findViewById(R.id.time_progress)
        comboTxt = view.findViewById(R.id.current_combo)
        endBtn = view.findViewById(R.id.end_session_btn)
        comboProgressDots = view.findViewById(R.id.combo_progress_dots)
        currentMoveTxt = view.findViewById(R.id.current_move)

        endBtn.setOnClickListener { returnToTrainingSelect() }

        if (time == null){
            timeProgressBar.visibility = View.INVISIBLE
        } else {
            timeRemaining = time.toInt()
            timeProgressBar.max = timeRemaining
            timeProgressBar.progress = timeRemaining
            timeProgressBar.isIndeterminate = false
        }

        timer = fixedRateTimer(period = 1000){

            if (time != null) {
                timeRemaining -= 1000

                if (timeRemaining <= 0) {
                    // Announce end
                    // Return to training select
                    returnToTrainingSelect()
                }

                timeProgressBar.progress = timeRemaining
            }

        }

        return view
    }

    private fun returnToTrainingSelect(){
        fragmentManager?.doTransaction {
            this.replace(R.id.fragment_holder,
                TrainingSelectFragment()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        tts = TextToSpeech(context, this)
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        if (tts.isSpeaking){
            tts.stop()
        }
        tts.shutdown()
    }

    private fun onPunch(punch: Punch){

        punches++

        if (comboTracker.matches(punch)){
            correctPunches++
            // TODO: Correct ding
            comboTracker.next()
            var idx = 0
            for (child in comboProgressDots.children){
                if (idx < comboTracker.index) {
                    (child as ImageView).setImageResource(R.drawable.ic_check)
                }
                idx++
            }
        } else {
            // TODO: Incorrect ding
        }

        if (comboTracker.isDone){
            nextCombo()
        } else {
            val currentMove = comboTracker.combo.punches[comboTracker.index]
            currentMoveTxt.text = "${currentMove.hand} ${currentMove.punchType}"
        }
    }

    private fun nextCombo(){
        val combo = getNextCombo()
        comboTracker = PunchComboTracker(combo)
        val currentMove = combo.punches.first()
        currentMoveTxt.text = "${currentMove.hand} ${currentMove.punchType}"
        announceNewCombo()
    }

    private fun getNextCombo(): PunchCombo {
        val idx = Random.nextInt(PunchCombos.combos.size)
        return PunchCombos.combos[idx]
    }

    private fun announceNewCombo(){
        tts.speak(comboTracker.combo.name, TextToSpeech.QUEUE_FLUSH, null)
        comboTxt.text = comboTracker.combo.name
        var idx = 0
        for (child in comboProgressDots.children){
            if (idx < comboTracker.combo.punches.size) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
            (child as ImageView).setImageResource(R.drawable.ic_not_complete)
            idx++
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            nextCombo()
        }
    }
}
