package com.example.itc__onl2_swd4_s3_1.ui.data

import com.example.itc__onl2_swd4_s3_1.ui.data.dao.DhikrDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.HabitDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.ProgressDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.dao.SalahDao
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.DhikrEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.ProgressEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.room.entity.SalahEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


class LocalDataSource @Inject constructor(
    private val dhikrDao: DhikrDao,
    private val salahDao: SalahDao,
    private val progressDao: ProgressDao,
    private val habitDao: HabitDao
) {
    annotation class Inject

    //Done DhikrDao----------------------------------------
    suspend fun getAllDhikr(): List<DhikrEntity> {
        return dhikrDao.getAllDhikr()
    }
    suspend fun getDhikrByCategory(category: String): List<DhikrEntity> {
        return dhikrDao.getDhikrByCategory(category)
    }
    suspend fun insertDhikr(dhikr: DhikrEntity) {
        dhikrDao.insertDhikr(dhikr)
    }
    suspend fun insertAll(dhikrList: List<DhikrEntity>) {
        dhikrDao.insertAll(dhikrList)
    }
    suspend fun deleteAll() {
        dhikrDao.deleteAll()
    }

    //Done SalahDao --------------------------------------------------------
    suspend fun insertOrUpdateSalah(salah: SalahEntity) {
        salahDao.insertOrUpdateSalah(salah)
    }
    suspend fun getSalahByDate(date: LocalDate): SalahEntity? {
        return salahDao.getSalahByDate(date)
    }
    suspend fun getAllSalah(): List<SalahEntity> {
        return salahDao.getAllSalah()
        }

    suspend fun deleteSalahByDate(date: LocalDate) {
        salahDao.deleteSalahByDate(date)
    }
    //Done progressDao--------------------------------------------------------

    suspend fun upsertProgress(progress: ProgressEntity) {
        progressDao.upsertProgress(progress)
    }
    suspend fun getProgressByDate(date: String): ProgressEntity? {
        return progressDao.getProgressByDate(date)
    }
    suspend fun getAllProgress(): List<ProgressEntity> {
        return progressDao.getAllProgress()
    }

    suspend fun getLongestStreak(): Int? {
        return progressDao.getLongestStreak()
    }
    suspend fun getCurrentStreakOnDate(date: String): Int? {
        return progressDao.getCurrentStreakOnDate(date)
    }
    suspend fun getFullyCompletedDates(): List<String> {
        return progressDao.getFullyCompletedDates()
    }
    suspend fun getCompletionPercentage(date: String): Float? {
        return progressDao.getCompletionPercentage(date)
    }
    suspend fun deleteProgressByDate(date: String) {
        progressDao.deleteProgressByDate(date)
    }
    //Done habitDao--------------------------------------------------------
    suspend fun insertHabit(habit: HabitEntity) {
        habitDao.insertHabit(habit)
    }
    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }
    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)

    }
    fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }
    suspend fun getHabitById(habitId: Int): HabitEntity? {
        return habitDao.getHabitById(habitId)
    }

//    suspend fun getUserById(id: Int): User? {
//        return userDao.getUserById(id)



}
