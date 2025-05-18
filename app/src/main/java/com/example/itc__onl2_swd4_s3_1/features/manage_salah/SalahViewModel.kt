package com.example.itc__onl2_swd4_s3_1.features.manage_salah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.data.entity.SalahEntity
import com.example.itc__onl2_swd4_s3_1.domain.repository.SalahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SalahViewModel @Inject constructor(
    private val repository: SalahRepository
) : ViewModel() {

    private val _completedDates = MutableStateFlow<List<LocalDate>>(emptyList())
    val completedDates: StateFlow<List<LocalDate>> = _completedDates

    private val _incompleteDates = MutableStateFlow<List<LocalDate>>(emptyList())
    val incompleteDates: StateFlow<List<LocalDate>> = _incompleteDates

    private val _selectedPrayers = MutableStateFlow<Set<String>>(emptySet())
    val selectedPrayers: StateFlow<Set<String>> = _selectedPrayers
    private var currentDate: String = ""
    fun loadInitialData(prayers: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val allRecords = repository.getAllRecords()
            _completedDates.value = allRecords.filter {
                it.prayers.split(",").size == prayers.size
            }.mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }

            _incompleteDates.value = allRecords.filter {
                val count = it.prayers.split(",").size
                count in 1 until prayers.size
            }.mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }
        }
    }

    fun loadPrayersForDate(date: String) {
        currentDate = date
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPrayersForDate(date)
            _selectedPrayers.value = result?.prayers?.split(",")?.toSet() ?: emptySet()
        }
    }


    fun togglePrayer(prayer: String, allPrayers: List<String>) {
        val updated = _selectedPrayers.value.toMutableSet().apply {
            if (contains(prayer)) remove(prayer) else add(prayer)
        }
        _selectedPrayers.value = updated

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdate(
                SalahEntity(
                    date = currentDate,
                    prayers = updated.joinToString(",")
                )
            )
            loadInitialData(allPrayers)
        }
    }

}
