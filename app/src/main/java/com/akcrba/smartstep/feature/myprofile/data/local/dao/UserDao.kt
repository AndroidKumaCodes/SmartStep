package com.akcrba.smartstep.feature.myprofile.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.akcrba.smartstep.feature.myprofile.data.local.entity.UserEntity
import org.koin.core.annotation.Singleton

@Dao
@Singleton
internal interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int = 1)
}
