package com.example.itc__onl2_swd4_s3_1.ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_days")
data class CompletedDayEntity(
    @PrimaryKey val date: String // format yyyy-MM-dd
)