package com.example.itc__onl2_swd4_s3_1.ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "salah_records")
data class SalahEntity(
    @PrimaryKey val date: String, // e.g. "2025-05-07"
    val prayers: String           // e.g. "Fajr,Dhuhr,Asr"
)
