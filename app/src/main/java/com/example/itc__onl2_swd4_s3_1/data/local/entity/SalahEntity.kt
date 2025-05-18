package com.example.itc__onl2_swd4_s3_1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "salah_records")
data class SalahEntity(
    @PrimaryKey val date: String,
    val prayers: String
)
