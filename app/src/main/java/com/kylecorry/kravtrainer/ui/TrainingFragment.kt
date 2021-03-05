package com.kylecorry.kravtrainer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.domain.combos.ComboTracker
import com.kylecorry.kravtrainer.domain.combos.PunchCombo
import com.kylecorry.kravtrainer.domain.combos.PunchCombos
import com.kylecorry.kravtrainer.domain.punches.*
import com.kylecorry.kravtrainer.domain.training.TrainingSessionRecorder
import com.kylecorry.kravtrainer.infrastructure.gloves.BluetoothGloves
import com.kylecorry.kravtrainer.infrastructure.traininghistory.TrainingSessionRepo
import com.kylecorry.kravtrainer.domain.training.TrainingTimer
import com.kylecorry.trailsensecore.infrastructure.sensors.asLiveData
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.random.Random


class TrainingFragment(private val time: Int?, private val leftAddress: String, private val rightAddress: String) : Fragment(), TextToSpeech.OnInitListener, Observer {

    private lateinit var timeProgressBar: ProgressBar
    private lateinit var comboTxt: TextView
    private lateinit var endBtn: Button
    private lateinit var comboProgressDots: LinearLayout
    private lateinit var currentMoveTxt: TextView

    private lateinit var comboTracker: ComboTracker
    private lateinit var tts: TextToSpeech
    private lateinit var gloves: BluetoothGloves
    private var timer: TrainingTimer? = null

    private var sessionRecorder =
        TrainingSessionRecorder()

    private var lastLeftPunch: PunchType? = null
    private var lastRightPunch: PunchType? = null

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var handler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training, container, false)

        timeProgressBar = view.findViewById(R.id.time_progress)
        comboTxt = view.findViewById(R.id.current_combo)
        endBtn = view.findViewById(R.id.end_session_btn)
        comboProgressDots = view.findViewById(R.id.combo_progress_dots)
        currentMoveTxt = view.findViewById(R.id.current_move)

        endBtn.setOnClickListener { completeTraining() }

        gloves = BluetoothGloves(requireContext(), leftAddress, rightAddress)

        if (time == null){
            timeProgressBar.visibility = View.INVISIBLE
        } else {
            timeProgressBar.max = time
            timeProgressBar.progress = time
            timer = TrainingTimer(time)
        }

        gloves.asLiveData().observe(viewLifecycleOwner, { onGlovesUpdate() })

        return view
    }

    private fun completeTraining(){
        val stats = sessionRecorder.createSessionReport()
        val db = TrainingSessionRepo(requireContext())
        db.create(stats)
        fragmentManager?.doTransaction {
            this.replace(
                R.id.fragment_holder,
                TrainingCompleteFragment(stats)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        tts = TextToSpeech(context, this)

        timer?.start()
        timer?.addObserver(this)

        handler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()

        timer?.deleteObserver(this)

        timer?.stop()
        if (tts.isSpeaking){
            tts.stop()
        }
        tts.shutdown()

        mediaPlayer?.release()
    }

    private fun onPunch(punch: Punch){
        if (comboTracker.matches(punch)){
            sessionRecorder.correct()
            playSound(R.raw.success)
            comboTracker.next()
            updateComboProgress()
        } else {
            sessionRecorder.incorrect()
            playSound(R.raw.fail)
        }

        if (comboTracker.isDone){
            sessionRecorder.completeCombo()
            nextCombo()
        }
    }

    private fun nextCombo(){
        val combo = getNextCombo()
        comboTracker =
            ComboTracker(combo)
        announceNewCombo()
    }

    private fun getNextCombo(): PunchCombo {
        val idx = Random.nextInt(PunchCombos.combos.size)
        return PunchCombos.combos[idx]
    }

    private fun announceNewCombo(){
        tts.speak(comboTracker.combo.name, TextToSpeech.QUEUE_FLUSH, null)
        if(comboTracker.combo.name.length > 10){
            comboTxt.textSize = 36.0f
        } else if (comboTracker.combo.name.length > 5){
            comboTxt.textSize = 50.0f
        } else {
            comboTxt.textSize = 112.0f
        }
        comboTxt.text = comboTracker.combo.name
        updateComboProgress()
    }

    private fun updateComboProgress(){
        var idx = 0
        for (child in comboProgressDots.children){
            if (idx < comboTracker.combo.punches.size) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
            if (idx < comboTracker.index){
                (child as ImageView).setImageResource(R.drawable.ic_complete)
            } else {
                (child as ImageView).setImageResource(R.drawable.ic_not_complete)
            }
            idx++
        }

        if (comboTracker.isDone) return

        val currentMove = comboTracker.currentPunch
        currentMoveTxt.text = "${currentMove?.hand} ${currentMove?.punchType}"
    }

    private fun playSound(id: Int){
        mediaPlayer = MediaPlayer.create(context, id)
        mediaPlayer?.setVolume(0.1f, 0.1f)
        mediaPlayer?.start()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            sessionRecorder.start()
            nextCombo()
        }
    }

    fun onGlovesUpdate(){
        if (!gloves.isConnected){
            // TODO: Alert user
            Toast.makeText(context, "Gloves disconnected", Toast.LENGTH_LONG).show()
            return
        }

        // TODO: Set connection indicator
        sessionRecorder.recordStrength(gloves.leftStrength)
        sessionRecorder.recordStrength(gloves.rightStrength)

        val leftPunchType = gloves.left
        val rightPunchType = gloves.right

        if (leftPunchType != null && leftPunchType != lastLeftPunch){
            onPunch(Punch.left(leftPunchType))
        }

        if (rightPunchType != null && rightPunchType != lastRightPunch){
            onPunch(Punch.right(rightPunchType))
        }

        lastLeftPunch = leftPunchType
        lastRightPunch = rightPunchType

    }

    fun onTimerUpdate(){
        val secondsLeft = timer?.remainingSeconds ?: 0

        if (secondsLeft <= 0) {
            // Announce end
            // Return to training select
            completeTraining()
        }

        timeProgressBar.progress = secondsLeft
    }

    override fun update(o: Observable?, arg: Any?) {
        if (o == timer) onTimerUpdate()
    }
}
