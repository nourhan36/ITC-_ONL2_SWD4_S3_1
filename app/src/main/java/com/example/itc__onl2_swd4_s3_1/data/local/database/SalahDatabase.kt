package com.example.itc__onl2_swd4_s3_1.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itc__onl2_swd4_s3_1.data.local.dao.SalahDao
import com.example.itc__onl2_swd4_s3_1.data.entity.SalahEntity

@Database(entities = [SalahEntity::class], version = 1)
abstract class SalahDatabase : RoomDatabase() {
    abstract fun salahDao(): SalahDao

    companion object {
        @Volatile private var INSTANCE: SalahDatabase? = null

        fun getDatabase(context: Context): SalahDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SalahDatabase::class.java,
                    "salah_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}