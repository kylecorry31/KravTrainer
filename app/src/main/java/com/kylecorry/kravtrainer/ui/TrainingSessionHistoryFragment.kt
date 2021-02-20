package com.kylecorry.kravtrainer.ui

import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kylecorry.kravtrainer.R
import com.kylecorry.kravtrainer.domain.training.TrainingSession
import com.kylecorry.kravtrainer.domain.training.RangedTrainingStatistic
import com.kylecorry.kravtrainer.infrastructure.traininghistory.TrainingSessionRepo
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.roundToInt


class TrainingSessionHistoryFragment : Fragment() {

    private lateinit var numPunchesTxt: TextView
    private lateinit var numCombosTxt: TextView
    private lateinit var accuracyTxt: TextView
    private lateinit var minutesTxt: TextView
    private lateinit var strengthTxt: TextView
    private lateinit var punchSpeedTxt: TextView
    private lateinit var chart: TimeChart
    private lateinit var sessionList: RecyclerView
    private lateinit var lastSessionDurationTxt: TextView

    private lateinit var sessionRepo: TrainingSessionRepo
    private lateinit var adapter: SessionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_training_session_history, container, false)

        numPunchesTxt = view.findViewById(R.id.num_punches)
        numCombosTxt = view.findViewById(R.id.num_combos)
        accuracyTxt = view.findViewById(R.id.accuracy)
        minutesTxt = view.findViewById(R.id.minutes)
        strengthTxt = view.findViewById(R.id.strength)
        punchSpeedTxt = view.findViewById(R.id.punches_per_second)
        chart = TimeChart(view.findViewById(R.id.history_chart), resources.getColor(R.color.colorPrimary, null))
        sessionList = view.findViewById(R.id.session_list)
        sessionList = view.findViewById(R.id.session_list)
        lastSessionDurationTxt = view.findViewById(R.id.last_session_duration)

        val layoutManager = LinearLayoutManager(context)

        sessionList.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            context,
            layoutManager.orientation
        )
        sessionList.addItemDecoration(dividerItemDecoration)
        return view
    }

    override fun onResume() {
        super.onResume()
        sessionRepo = TrainingSessionRepo(requireContext())
        val sessions = sessionRepo.getAll()
        val dailySessions = getDailySessions(sessions)
        updateUI(sessions)
        updateChart(dailySessions)

        adapter = SessionAdapter(sessions.filter { it.punches > 0 })
        sessionList.adapter = adapter
    }

    private fun updateUI(sessions: List<TrainingSession>){
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

        if (duration.seconds == 0L){
            punchSpeedTxt.text = "0"
        } else {
            punchSpeedTxt.text = String.format("%.1f", punches / duration.seconds.toFloat())
        }

        val lastSession = sessions.firstOrNull { it.punches > 0 }

        if (lastSession != null){
            lastSessionDurationTxt.text = "${DurationFormatter.format(lastSession.duration)} • ${TimeAgoFormatter.format(lastSession.date)}"
        }
    }

    private fun getDailySessions(sessions: List<TrainingSession>): List<TrainingSession> {
        val dailySessions = mutableMapOf<LocalDate, MutableList<TrainingSession>>()

        for (session in sessions){
            val date = session.date.toLocalDate()

            if (!dailySessions.containsKey(date)){
                dailySessions[date] = mutableListOf()
            }

            dailySessions[date]?.add(session)
        }

        val dailySessionsList = mutableListOf<TrainingSession>()

        for (dailySession in dailySessions){
            var incorrect = 0
            var correct = 0
            var combos = 0
            var duration = Duration.ofSeconds(0)
            var force = 0f

            for (session in dailySession.value){
                incorrect += session.incorrect
                correct += session.correct
                combos += session.combos
                duration = duration.plus(session.duration)
                force = max(force, session.strength)
            }

            dailySessionsList.add(TrainingSession(-1, dailySession.key.atStartOfDay(), duration, incorrect, correct, force, combos))
        }

        return dailySessionsList.sortedBy { it.date }
    }

    private fun updateChart(sessions: List<TrainingSession>){
        val today = LocalDate.now()
        val data = mutableListOf<Number>()

        for (i in 0 until 30){
            val session = getSession(today.minusDays(i.toLong()), sessions)
            if (session != null){
                data.add(session.duration.seconds / 60.0f)
            } else {
                data.add(0f)
            }
        }

        val week = RangedTrainingStatistic(today.minusDays(6), data.reversed())

        chart.plot(week)
    }

    private fun getSession(date: LocalDate, sessions: List<TrainingSession>): TrainingSession? {
        return sessions.firstOrNull { it.date.toLocalDate() == date }
    }


    // Chart data retrievers
    private fun getAccuracyData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.accuracy > 0 }.map { Pair(it.date, it.accuracy * 100) }
    }

    private fun getPunchData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.punches > 0 }.map { Pair(it.date, it.punches) }
    }

    private fun getPunchSpeedData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.punches > 0 }.map { Pair(it.date, it.punchesPerSecond) }
    }

    private fun getStrengthData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.punches > 0 }.map { Pair(it.date, it.strength / SensorManager.GRAVITY_EARTH) }
    }

    private fun getDurationData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.punches > 0 }.map { Pair(it.date, it.duration.toMinutes()) }
    }

    private fun getComboData(sessions: List<TrainingSession>): List<Pair<LocalDateTime, Number>> {
        return sessions.filter { it.punches > 0 }.map { Pair(it.date, it.combos) }
    }


    // Adapters
    inner class SessionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val timeTxt: TextView = itemView.findViewById(R.id.session_time)
        private val durationTxt: TextView = itemView.findViewById(R.id.duration)
        private val accuracyTxt: TextView = itemView.findViewById(R.id.accuracy)
        private val combosTxt: TextView = itemView.findViewById(R.id.num_combos)
        private val punchesTxt: TextView = itemView.findViewById(R.id.num_punches)


        fun bindToSession(session: TrainingSession){
            timeTxt.text = session.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a"))
            durationTxt.text = DurationFormatter.format(session.duration)
            accuracyTxt.text = "${(session.accuracy * 100).roundToInt()}% correct"
            combosTxt.text = "${session.combos} combos"
            punchesTxt.text = "${session.punches} punches"
        }
    }

    inner class SessionAdapter(private val sessions: List<TrainingSession>): RecyclerView.Adapter<SessionHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
            val view = layoutInflater.inflate(R.layout.list_item_training_session, parent, false)
            return SessionHolder(view)
        }

        override fun getItemCount(): Int {
            return sessions.size
        }

        override fun onBindViewHolder(holder: SessionHolder, position: Int) {
            val session = sessions[position]
            holder.bindToSession(session)
        }
    }

}
