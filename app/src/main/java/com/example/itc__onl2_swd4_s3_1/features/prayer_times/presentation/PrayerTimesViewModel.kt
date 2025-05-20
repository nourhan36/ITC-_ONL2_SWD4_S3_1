package com.example.itc__onl2_swd4_s3_1.features.prayer_times.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itc__onl2_swd4_s3_1.domain.model.PrayerTime
import com.example.itc__onl2_swd4_s3_1.domain.repository.PrayerTimesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerTimesViewModel @Inject constructor(
    private val repository: PrayerTimesRepository
) : ViewModel() {

    private val _prayerTimes = MutableStateFlow<List<PrayerTime>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTime>> = _prayerTimes.asStateFlow()

    fun loadPrayerTimes(city: String) {
        viewModelScope.launch {
            try {
                val times = repository.getPrayerTimes(city)
                _prayerTimes.value = times
            } catch (e: Exception) {
                _prayerTimes.value = emptyList()
            }
        }
    }
}
