package com.example.itc__onl2_swd4_s3_1.ui.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dhikr_table")
data class DhikrEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val count: Int,
    val translation: String,
    val content: String,
    val description: String
)
