package com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.CompletedDayEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = HabitDatabase.getDatabase(application).habitDao()
    private val _currentDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    private val _selectedFilter = mutableStateOf("All")
    // In HabitViewModel
    val todayHabits: Flow<List<HabitEntity>> =
        dao.getHabitsByDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE))


    val selectedFilter: String get() = _selectedFilter.value
    val today get() = _currentDate.value

    val allHabits: Flow<List<HabitEntity>> = _currentDate.flatMapLatest { date ->
        dao.getHabitsByDate(date)
    }
    val completedHabits: Flow<List<HabitEntity>> = _currentDate.flatMapLatest { date ->
        dao.getCompletedHabitsByDate(date)
    }
    val incompleteHabits: Flow<List<HabitEntity>> = _currentDate.flatMapLatest { date ->
        dao.getIncompleteHabitsByDate(date)
    }

    val filteredHabits: Flow<List<HabitEntity>>
        get() = when (selectedFilter) {
            "Complete" -> completedHabits
            "Incomplete" -> incompleteHabits
            else -> allHabits
        }

    fun refreshDate() {
        _currentDate.value = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }


    val completedDayDao = HabitDatabase.getDatabase(application).completedDayDao()

    val allCompletedDays: Flow<List<String>> =
        completedDayDao.getAllCompletedDays().map { list -> list.map { it.date } }
    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }




    fun deleteAllHabits() {
        viewModelScope.launch {
            dao.deleteAllHabits()
        }
    }

    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            dao.updateHabit(habit.copy(isCompleted = !habit.isCompleted))
            markDayCompletedIfAllHabitsDone()
        }
    }
    fun deleteOldHabits(today: String) {
        viewModelScope.launch {
            dao.deleteOldHabits(today)
        }
    }

    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.insertHabit(habit)
        }
    }


    fun markDayCompletedIfAllHabitsDone() {
        viewModelScope.launch {
            val habits = dao.getAllHabitsNow()
            if (habits.isNotEmpty() && habits.all { it.isCompleted }) {
                val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                completedDayDao.insert(CompletedDayEntity(today))
            }
        }
    }

}

