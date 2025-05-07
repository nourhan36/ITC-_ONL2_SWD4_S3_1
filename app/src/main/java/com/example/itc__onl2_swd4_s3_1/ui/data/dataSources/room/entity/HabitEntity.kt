package com.example.itc__onl2_swd4_s3_1.ui.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val startTime: String,
    val repeatType: String,
    val duration: Int,
    val reminderTime: String
)
