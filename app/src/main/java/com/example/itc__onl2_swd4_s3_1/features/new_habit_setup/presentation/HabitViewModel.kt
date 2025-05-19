package com.example.itc__onl2_swd4_s3_1.features.new_habit_setup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity
import com.example.itc__onl2_swd4_s3_1.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration
import javax.inject.Inject
import androidx.annotation.StringRes
import com.example.itc__onl2_swd4_s3_1.R

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(HabitFilter.ALL)
    val selectedFilter: StateFlow<HabitFilter> = _selectedFilter

    private val _currentDate = MutableStateFlow(LocalDate.now().toString())
    val activeHabits: Flow<List<HabitEntity>> =
        _currentDate.flatMapLatest { date -> habitRepository.getActiveHabits(date) }

    val filteredHabits: Flow<List<HabitEntity>> =
        activeHabits.combine(selectedFilter) { habits, filter ->
            when (filter) {
                HabitFilter.COMPLETE -> habits.filter { it.isCompleted }
                HabitFilter.INCOMPLETE -> habits.filter { !it.isCompleted }
                HabitFilter.ALL -> habits
            }
        }

    private val _completedDays = MutableStateFlow<List<String>>(emptyList())
    val allCompletedDays: StateFlow<List<String>> = _completedDays.asStateFlow()

    init {
        refreshDate()
        observeCompletedDays()
        startMidnightObserver()
    }

    private fun observeCompletedDays() {
        viewModelScope.launch {
            _completedDays.value = habitRepository.getCompletedDays()
        }
    }

    fun refreshDate() {
        _currentDate.value = LocalDate.now().toString()
    }

    fun setFilter(filter: HabitFilter) {
        _selectedFilter.value = filter
    }

    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitRepository.insertHabit(habit)
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitRepository.updateHabit(habit)
            habitRepository.markDayIfCompleted()
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habit)
            habitRepository.markDayIfCompleted()
        }
    }

    fun toggleHabitCompletion(habit: HabitEntity) {
        viewModelScope.launch {
            val updated = habit.copy(isCompleted = !habit.isCompleted)
            habitRepository.updateHabit(updated)
            habitRepository.markDayIfCompleted()
        }
    }

    private fun startMidnightObserver() {
        val now = LocalDateTime.now()
        val tomorrow = now.toLocalDate().plusDays(1).atStartOfDay()
        val delayMillis = Duration.between(now, tomorrow).toMillis()

        viewModelScope.launch {
            delay(delayMillis)
            refreshDate()
            startMidnightObserver()
        }
    }
}



enum class HabitFilter {
    ALL, COMPLETE, INCOMPLETE
}

@StringRes
fun HabitFilter.getLabelRes(): Int = when (this) {
    HabitFilter.ALL -> R.string.all
    HabitFilter.COMPLETE -> R.string.complete
    HabitFilter.INCOMPLETE -> R.string.incomplete
}

