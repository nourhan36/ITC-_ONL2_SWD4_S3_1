package com.example.itc__onl2_swd4_s3_1.ui.data.room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.dao.DhikrDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.HabitDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.ProgressDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.SalahDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.DhikrEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.ProgressEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.SalahEntity

@Database(
    entities = [
        DhikrEntity::class,
        SalahEntity::class,
        ProgressEntity::class,
        HabitEntity::class],
    version = 1)
abstract class RamadanTrackerDatabase: RoomDatabase() {
    abstract fun getDhikrDao(): DhikrDao
    abstract fun getSalahDao(): SalahDao
    abstract fun getProgressDao(): ProgressDao
    abstract fun getHabitDao(): HabitDao



}