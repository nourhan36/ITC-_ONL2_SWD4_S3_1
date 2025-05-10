package com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = HabitDatabase.getDatabase(application).habitDao()

    private val _selectedFilter = mutableStateOf("All")
    val selectedFilter: String get() = _selectedFilter.value

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val completedHabits: Flow<List<HabitEntity>> = dao.getCompletedHabits()
    val incompleteHabits: Flow<List<HabitEntity>> = dao.getIncompleteHabits()

    val filteredHabits: Flow<List<HabitEntity>>
        get() = when (selectedFilter) {
            "Complete" -> completedHabits
            "Incomplete" -> incompleteHabits
            else -> allHabits
        }
    fun deleteAllHabits() {
        viewModelScope.launch {
            dao.deleteAllHabits()
        }
    }

    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            dao.updateHabit(habit.copy(isCompleted = !habit.isCompleted))
        }
    }

    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.insertHabit(habit)
        }
    }
}

