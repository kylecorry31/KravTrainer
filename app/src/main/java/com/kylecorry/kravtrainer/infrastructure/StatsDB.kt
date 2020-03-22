package com.kylecorry.kravtrainer.infrastructure

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kylecorry.kravtrainer.Constants
import com.kylecorry.kravtrainer.domain.models.TrainingStats
import java.time.LocalDateTime

class StatsDB(ctx: Context) {

    private val db = StatsDBHelper(ctx).writableDatabase

    val stats: List<TrainingStats>
        get(){
            val cursor = query(null, null, Constants.STATS_CREATE_TIME)
            val stats = mutableListOf<TrainingStats>()
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

    fun create(stats: TrainingStats){
        val values = getContentValues(stats)
        values.remove(Constants.STATS_ID)
        db.insert(Constants.STATS_TABLE, null, values)
    }

    fun delete(stats: TrainingStats){
        db.delete(
            Constants.STATS_TABLE,
            "${Constants.STATS_ID} = ?",
            arrayOf(stats.id.toString()))
    }

    fun get(id: Int): TrainingStats? {
        val cursor = query("${Constants.STATS_ID} = ?", arrayOf(id.toString()))
        if (cursor.count != 0){
            cursor.moveToFirst()
            return cursor.getStats()
        }
        return null
    }

    private fun getContentValues(stats: TrainingStats): ContentValues {
        val values = ContentValues()
        values.put(Constants.STATS_ID, stats.id)
        values.put(Constants.STATS_CREATE_TIME, stats.date.toString())
        values.put(Constants.STATS_CORRECT, stats.correct)
        values.put(Constants.STATS_INCORRECT, stats.incorrect)
        values.put(Constants.STATS_COMBOS, stats.combos)
        values.put(Constants.STATS_DURATION, stats.seconds)
        values.put(Constants.STATS_FORCE, stats.strength)
        return values
    }

    private fun query(where: String?, whereArgs: Array<String>?, orderBy: String? = null): StatsCursor {
        val cursor = db.query(
            Constants.STATS_TABLE,
            null,
            where,
            whereArgs,
            null,
            null,
            orderBy
        )

        return StatsCursor(cursor)
    }

}

private class StatsDBHelper(ctx: Context): SQLiteOpenHelper(ctx, "shadow_boxer", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db ?: return
        db.execSQL("create table " + Constants.STATS_TABLE + "(" +
                Constants.STATS_ID + " integer primary key autoincrement, " +
                Constants.STATS_CREATE_TIME + ", " +
                Constants.STATS_DURATION + ", " +
                Constants.STATS_INCORRECT + ", " +
                Constants.STATS_CORRECT + ", " +
                Constants.STATS_FORCE + ", " +
                Constants.STATS_COMBOS +
                ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db ?: return
        db.execSQL("drop table " + Constants.STATS_TABLE)
        this.onCreate(db)
    }
}

private class StatsCursor(cursor: Cursor): CursorWrapper(cursor) {

    fun getStats(): TrainingStats {

        val id = getInt(getColumnIndex(Constants.STATS_ID))
        val createdDate = getString(getColumnIndex(Constants.STATS_CREATE_TIME))
        val combos = getInt(getColumnIndex(Constants.STATS_COMBOS))
        val duration = getInt(getColumnIndex(Constants.STATS_DURATION))
        val correct = getInt(getColumnIndex(Constants.STATS_CORRECT))
        val incorrect = getInt(getColumnIndex(Constants.STATS_INCORRECT))
        val strength = getFloat(getColumnIndex(Constants.STATS_FORCE))

        return TrainingStats(id, LocalDateTime.parse(createdDate), correct, incorrect, combos, strength, duration)
    }
}