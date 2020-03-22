package com.kylecorry.kravtrainer.infrastructure

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kylecorry.kravtrainer.Constants
import com.kylecorry.kravtrainer.domain.models.TrainingSession
import java.time.Duration
import java.time.LocalDateTime

class TrainingSessionRepo(ctx: Context) {

    private val db = SessionDBHelper(ctx).writableDatabase

    fun getAll(): List<TrainingSession> {
        val cursor = query()
        val stats = mutableListOf<TrainingSession>()
        cursor.use { c ->
            if (c.count == 0){
                return stats
            }
            c.moveToFirst()
            while(!c.isAfterLast){
                stats.add(c.getStats())
                c.moveToNext()
            }
        }
        return stats
    }

    fun create(session: TrainingSession){
        db.insert(Constants.TRAINING_SESSION_TABLE, null, getContentValues(session))
    }

    fun update(session: TrainingSession){
        val values = getContentValues(session)
        db.update(Constants.TRAINING_SESSION_TABLE, values, "${Constants.TRAINING_SESSION_ID} = ?", arrayOf(session.id.toString()))
    }

    fun delete(session: TrainingSession){
        db.delete(
            Constants.TRAINING_SESSION_TABLE,
            "${Constants.TRAINING_SESSION_ID} = ?",
            arrayOf(session.id.toString()))
    }

    fun get(id: Int): TrainingSession? {
        val cursor = query("${Constants.TRAINING_SESSION_ID} = ?", arrayOf(id.toString()))
        if (cursor.count != 0){
            cursor.moveToFirst()
            return cursor.getStats()
        }
        return null
    }

    private fun getContentValues(session: TrainingSession): ContentValues {
        val values = ContentValues()
        values.put(Constants.TRAINING_SESSION_CREATE_TIME, session.date.toString())
        values.put(Constants.TRAINING_SESSION_CORRECT, session.correct)
        values.put(Constants.TRAINING_SESSION_INCORRECT, session.incorrect)
        values.put(Constants.TRAINING_SESSION_COMBOS, session.combos)
        values.put(Constants.TRAINING_SESSION_DURATION, session.duration.seconds)
        values.put(Constants.TRAINING_SESSION_FORCE, session.strength)
        return values
    }

    private fun query(where: String? = null, whereArgs: Array<String>? = null, orderBy: String? = null): SessionCursor {
        val cursor = db.query(
            Constants.TRAINING_SESSION_TABLE,
            null,
            where,
            whereArgs,
            null,
            null,
            orderBy
        )

        return SessionCursor(cursor)
    }

    private class SessionDBHelper(ctx: Context): SQLiteOpenHelper(ctx, Constants.DB_NAME, null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db ?: return
            db.execSQL("create table " + Constants.TRAINING_SESSION_TABLE + "(" +
                    Constants.TRAINING_SESSION_ID + " integer primary key autoincrement, " +
                    Constants.TRAINING_SESSION_CREATE_TIME + ", " +
                    Constants.TRAINING_SESSION_DURATION + ", " +
                    Constants.TRAINING_SESSION_INCORRECT + ", " +
                    Constants.TRAINING_SESSION_CORRECT + ", " +
                    Constants.TRAINING_SESSION_FORCE + ", " +
                    Constants.TRAINING_SESSION_COMBOS +
                    ")"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db ?: return
            db.execSQL("drop table " + Constants.TRAINING_SESSION_TABLE)
            this.onCreate(db)
        }
    }

    private class SessionCursor(cursor: Cursor): CursorWrapper(cursor) {

        fun getStats(): TrainingSession {

            val id = getInt(getColumnIndex(Constants.TRAINING_SESSION_ID))
            val createdDate = getString(getColumnIndex(Constants.TRAINING_SESSION_CREATE_TIME))
            val combos = getInt(getColumnIndex(Constants.TRAINING_SESSION_COMBOS))
            val seconds = getLong(getColumnIndex(Constants.TRAINING_SESSION_DURATION))
            val correct = getInt(getColumnIndex(Constants.TRAINING_SESSION_CORRECT))
            val incorrect = getInt(getColumnIndex(Constants.TRAINING_SESSION_INCORRECT))
            val strength = getFloat(getColumnIndex(Constants.TRAINING_SESSION_FORCE))

            return TrainingSession(
                id,
                LocalDateTime.parse(createdDate),
                Duration.ofSeconds(seconds),
                incorrect,
                correct,
                strength,
                combos
            )
        }
    }

}