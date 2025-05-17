package com.example.itc__onl2_swd4_s3_1.ui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.dao.CompletedDayDao
import com.example.itc__onl2_swd4_s3_1.ui.data.dao.HabitDao
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.CompletedDayEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, CompletedDayEntity::class], // ← أضف CompletedDayEntity هنا
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun completedDayDao(): CompletedDayDao


    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}