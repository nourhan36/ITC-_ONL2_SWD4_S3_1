package com.example.itc__onl2_swd4_s3_1.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 0, // ثابت لأن فيه سطر واحد فقط
    val isDarkMode: Boolean
)
