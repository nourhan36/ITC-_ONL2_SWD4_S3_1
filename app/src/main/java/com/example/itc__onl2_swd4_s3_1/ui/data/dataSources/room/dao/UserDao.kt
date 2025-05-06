//package com.example.itc__onl2_swd4_s3_1.ui.data.room.dao
//
//import com.example.itc__onl2_swd4_s3_1.ui.data.entity.UserEntity
//import androidx.room.*
//import com.example.itc__onl2_swd4_s3_1.ui.data.entity.User
//
//@Dao
//interface UserDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(user: User): Long
//
//    @Update
//    suspend fun updateUser(user: User)
//
//    @Delete
//    suspend fun deleteUser(user: User)
//
//    @Query("SELECT * FROM users WHERE id = :id")
//    suspend fun getUserById(id: Int): User?
//
//    @Query("SELECT * FROM users ORDER BY createdAt DESC")
//    suspend fun getAllUsers(): List<User>
//
//    @Query("DELETE FROM users")
//    suspend fun deleteAllUsers()
//}
