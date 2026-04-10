package com.akcrba.smartstep.feature.myprofile.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.akcrba.smartstep.feature.myprofile.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

@Dao
@Singleton
internal interface UserDao {

    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM users WHERE id = 1")
    suspend fun deleteUser()
}
