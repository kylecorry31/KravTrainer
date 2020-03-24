package com.kylecorry.kravtrainer.infrastructure.traininghistory

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kylecorry.kravtrainer.domain.training.TrainingSession
import java.time.Duration
import java.time.LocalDateTime

class TrainingSessionRepo(private val ctx: Context) {

    fun getAll(): List<TrainingSession> {
        val db = SessionDBHelper(ctx).readableDatabase
        val cursor = query(db)
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
        db.close()
        return stats.sortedByDescending { it.date }
    }

    fun create(session: TrainingSession){
        val db = SessionDBHelper(ctx).writableDatabase
        db.insert(TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE, null, getContentValues(session))
        db.close()
    }

    fun update(session: TrainingSession){
        val db = SessionDBHelper(ctx).writableDatabase
        val values = getContentValues(session)
        db.update(TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE, values, "${TrainingSessionDatabaseConstants.TRAINING_SESSION_ID} = ?", arrayOf(session.id.toString()))
        db.close()
    }

    fun delete(session: TrainingSession){
        val db = SessionDBHelper(ctx).writableDatabase
        db.delete(
            TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE,
            "${TrainingSessionDatabaseConstants.TRAINING_SESSION_ID} = ?",
            arrayOf(session.id.toString()))
        db.close()
    }

    fun get(id: Int): TrainingSession? {
        val db = SessionDBHelper(ctx).readableDatabase
        val cursor = query(db, "${TrainingSessionDatabaseConstants.TRAINING_SESSION_ID} = ?", arrayOf(id.toString()))
        if (cursor.count != 0){
            cursor.moveToFirst()
            val stats = cursor.getStats()
            db.close()
            return stats
        }
        db.close()
        return null
    }

    private fun getContentValues(session: TrainingSession): ContentValues {
        val values = ContentValues()
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_CREATE_TIME, session.date.toString())
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_CORRECT, session.correct)
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_INCORRECT, session.incorrect)
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_COMBOS, session.combos)
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_DURATION, session.duration.seconds)
        values.put(TrainingSessionDatabaseConstants.TRAINING_SESSION_FORCE, session.strength)
        return values
    }

    private fun query(db: SQLiteDatabase, where: String? = null, whereArgs: Array<String>? = null, orderBy: String? = null): SessionCursor {
        val cursor = db.query(
            TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE,
            null,
            where,
            whereArgs,
            null,
            null,
            orderBy
        )

        return SessionCursor(
            cursor
        )
    }

    private class SessionDBHelper(ctx: Context): SQLiteOpenHelper(ctx, TrainingSessionDatabaseConstants.DB_NAME, null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db ?: return
            db.execSQL("create table " + TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE + "(" +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_ID + " integer primary key autoincrement, " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_CREATE_TIME + ", " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_DURATION + ", " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_INCORRECT + ", " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_CORRECT + ", " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_FORCE + ", " +
                    TrainingSessionDatabaseConstants.TRAINING_SESSION_COMBOS +
                    ")"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db ?: return
            db.execSQL("drop table " + TrainingSessionDatabaseConstants.TRAINING_SESSION_TABLE)
            this.onCreate(db)
        }
    }

    private class SessionCursor(cursor: Cursor): CursorWrapper(cursor) {

        fun getStats(): TrainingSession {

            val id = getInt(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_ID))
            val createdDate = getString(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_CREATE_TIME))
            val combos = getInt(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_COMBOS))
            val seconds = getLong(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_DURATION))
            val correct = getInt(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_CORRECT))
            val incorrect = getInt(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_INCORRECT))
            val strength = getFloat(getColumnIndex(TrainingSessionDatabaseConstants.TRAINING_SESSION_FORCE))

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