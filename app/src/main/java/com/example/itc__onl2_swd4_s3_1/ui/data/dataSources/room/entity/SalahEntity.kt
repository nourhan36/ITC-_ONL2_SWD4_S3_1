package com.example.itc__onl2_swd4_s3_1.ui.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "salah_records")
data class SalahEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val prayers: String
)
