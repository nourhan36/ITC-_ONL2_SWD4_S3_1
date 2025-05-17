package com.example.itc__onl2_swd4_s3_1.ui.ui.newHabitSetup

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.example.itc__onl2_swd4_s3_1.ui.data.HabitDatabase
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.CompletedDayEntity
import com.example.itc__onl2_swd4_s3_1.ui.data.entity.HabitEntity
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






    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val completedHabits: Flow<List<HabitEntity>> = dao.getCompletedHabits()
    val incompleteHabits: Flow<List<HabitEntity>> = dao.getIncompleteHabits()

    val today get() = _currentDate.value

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
            markDayCompletedIfAllHabitsDone() // ✅ بعد ما تمسح عادة، تأكد من تحديث أيام الإنجاز
        }
    }


    fun editHabit(habit: HabitEntity) = viewModelScope.launch {
        dao.updateHabit(habit)
    }
    private val _currentDate = MutableStateFlow(LocalDate.now().toString())
    val activeHabits = _currentDate.flatMapLatest { date ->
        dao.getActiveHabits(date)
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.updateHabit(habit)
            markDayCompletedIfAllHabitsDone() // ✅ علشان نحدث حالة اليوم بعد التعديل
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

    // Replace existing filteredHabits with:


    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }


    val completedDayDao = HabitDatabase.getDatabase(application).completedDayDao()

    val allCompletedDays: Flow<List<String>> =
        completedDayDao.getAllCompletedDays().map { list -> list.map { it.date } }





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
            val activeHabits = dao.getActiveHabitsNow(today)

            if (activeHabits.isNotEmpty()) {
                if (activeHabits.all { it.isCompleted }) {
                    completedDayDao.insert(CompletedDayEntity(today))
                } else {
                    completedDayDao.deleteByDate(today)
                }
            } else {
                // ✅ No habits today, remove the day from completed list if exists
                completedDayDao.deleteByDate(today)
            }
        }
    }


}

