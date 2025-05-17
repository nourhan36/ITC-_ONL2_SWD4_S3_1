package com.example.itc__onl2_swd4_s3_1.features.new_habit_setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.data.local.database.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.data.entity.CompletedDayEntity
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.combine

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = HabitDatabase.getDatabase(application).habitDao()


    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
            markDayCompletedIfAllHabitsDone()
        }
    }


    private val _currentDate = MutableStateFlow(LocalDate.now().toString())

    val activeHabits = _currentDate.flatMapLatest { date ->
        dao.getActiveHabits(date)
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.updateHabit(habit)
            markDayCompletedIfAllHabitsDone()
        }
    }

    fun refreshDate() {
        _currentDate.value = LocalDate.now().toString()
    }
    val filteredHabits: Flow<List<HabitEntity>> = activeHabits.combine(selectedFilter) { activeList, filter ->
        when (filter) {
            "Complete" -> activeList.filter { it.isCompleted }
            "Incomplete" -> activeList.filter { !it.isCompleted }
            else -> activeList
        }
    }
    init {
        startMidnightObserver()
    }

    private fun startMidnightObserver() {
        val now = LocalDate.now().atStartOfDay()
        val tomorrow = now.plusDays(1)
        val delayMillis = java.time.Duration.between(java.time.LocalDateTime.now(), tomorrow).toMillis()

        viewModelScope.launch {
            kotlinx.coroutines.delay(delayMillis)
            refreshDate()
            startMidnightObserver()
        }
    }



    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }


    val completedDayDao = HabitDatabase.getDatabase(application).completedDayDao()

    val allCompletedDays: Flow<List<String>> =
        completedDayDao.getAllCompletedDays().map { list -> list.map { it.date } }


    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            dao.updateHabit(habit.copy(isCompleted = !habit.isCompleted))
            markDayCompletedIfAllHabitsDone()
        }
    }


    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.insertHabit(habit)
        }
    }


    fun markDayCompletedIfAllHabitsDone() {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val activeHabits = dao.getActiveHabitsNow(today)

            if (activeHabits.isNotEmpty()) {
                if (activeHabits.all { it.isCompleted }) {
                    completedDayDao.insert(CompletedDayEntity(today))
                } else {
                    completedDayDao.deleteByDate(today)
                }
            } else {
                completedDayDao.deleteByDate(today)
            }
        }
    }


}

