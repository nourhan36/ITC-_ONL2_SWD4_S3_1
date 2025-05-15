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

    private val _selectedFilter = mutableStateOf("All")




    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val completedHabits: Flow<List<HabitEntity>> = dao.getCompletedHabits()
    val incompleteHabits: Flow<List<HabitEntity>> = dao.getIncompleteHabits()
    val selectedFilter: String get() = _selectedFilter.value
    val today get() = _currentDate.value


    private val _currentDate = MutableStateFlow(LocalDate.now().toString())
    val activeHabits = _currentDate.flatMapLatest { date ->
        dao.getActiveHabits(date)
    }

    fun refreshDate() {
        _currentDate.value = LocalDate.now().toString()
    }
    val filteredHabits: Flow<List<HabitEntity>>
        get() = when (selectedFilter) {
            "Complete" -> completedHabits
            "Incomplete" -> incompleteHabits
            else -> allHabits
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


    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.insertHabit(habit)
        }
    }


    fun markDayCompletedIfAllHabitsDone() {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val activeHabits = dao.getActiveHabitsNow(today) // Corrected variable name

            if (activeHabits.isNotEmpty() && activeHabits.all { it.isCompleted }) {
                completedDayDao.insert(CompletedDayEntity(today))
            } else {
                completedDayDao.deleteByDate(today) // Corrected function name
            }
        }
    }

}

