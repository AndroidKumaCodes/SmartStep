package com.akcrba.smartstep.feature.myprofile.data.local

import android.annotation.SuppressLint
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.akcrba.smartstep.feature.myprofile.data.local.dao.UserDao
import com.akcrba.smartstep.feature.myprofile.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false,
)
@SuppressLint("RestrictedApi") // Suppresses the warning for the restricted super constructor
internal abstract class SmartStepDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}
