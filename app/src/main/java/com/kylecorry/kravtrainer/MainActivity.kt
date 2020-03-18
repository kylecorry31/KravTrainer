package com.kylecorry.kravtrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var currentComboTxt: TextView
    private lateinit var connectedText: TextView
    private lateinit var comboProgressBar: ProgressBar
    private lateinit var startBtn: Button

    private lateinit var comboTracker: PunchComboTracker
    private lateinit var tts: TextToSpeech

    private var started: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentComboTxt = findViewById(R.id.current_combo)
        connectedText = findViewById(R.id.connection_status)
        startBtn = findViewById(R.id.start_button)
        comboProgressBar = findViewById(R.id.combo_progress)

        resetUI()

        tts = TextToSpeech(this, this)
    }

    private fun onPunch(punch: Punch){
        if (!started) return

        if (comboTracker.matches(punch)){
            // TODO: Correct ding
            comboTracker.next()
            comboProgressBar.progress = (comboTracker.progress * 100).roundToInt()
        } else {
            // TODO: Incorrect ding
        }

        if (comboTracker.isDone){
            nextCombo()
        }
    }

    private fun nextCombo(){
        val combo = getNextCombo()
        comboTracker = PunchComboTracker(combo)
        announceNewCombo()
    }

    private fun getNextCombo(): PunchCombo {
        val idx = Random.nextInt(PunchCombos.combos.size)
        return PunchCombos.combos[idx]
    }

    private fun announceNewCombo(){
        tts.speak(comboTracker.combo.name, TextToSpeech.QUEUE_FLUSH, null)
        currentComboTxt.text = comboTracker.combo.name
        comboProgressBar.progress = 0
        comboProgressBar.visibility = View.VISIBLE
    }

    private fun resetUI(){
        startBtn.text = getString(R.string.button_start)
        currentComboTxt.text = getString(R.string.ready)
        comboProgressBar.progress = 0
        comboProgressBar.visibility = View.INVISIBLE
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            connectedText.text = getString(R.string.gloves_connected)
            startBtn.setOnClickListener {
                if (!started) {
                    started = true
                    nextCombo()
                    startBtn.text = getString(R.string.button_stop)
                } else {
                    started = false
                    resetUI()
                }
            }
        }
    }
}
