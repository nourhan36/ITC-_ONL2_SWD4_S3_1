package com.example.itc__onl2_swd4_s3_1.ui.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
class ProgressEntity(
    @PrimaryKey(autoGenerate = false)
    val date: String,
    val completedHabitsCount: Int,
    val totalHabitsCount: Int,
    val completedSalahCount: Int,
    val totalSalahCount: Int = 5,
    val isFullyCompleted: Boolean,
    val currentStreak: Int,
    val longestStreak: Int
)