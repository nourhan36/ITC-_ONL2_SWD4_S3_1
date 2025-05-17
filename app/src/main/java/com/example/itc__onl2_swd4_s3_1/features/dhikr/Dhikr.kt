package com.example.itc__onl2_swd4_s3_1.features.dhikr

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


data class Dhikr(
    val category: String,
    val count: String,
    val description: String,
    val content: String,
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)
)