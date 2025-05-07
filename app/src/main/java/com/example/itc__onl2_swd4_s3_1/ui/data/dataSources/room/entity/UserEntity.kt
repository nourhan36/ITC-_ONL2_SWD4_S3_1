package com.example.itc__onl2_swd4_s3_1.ui.data.room.entity

import androidx.room.Entity


@Entity(tableName = "users")
data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
)

