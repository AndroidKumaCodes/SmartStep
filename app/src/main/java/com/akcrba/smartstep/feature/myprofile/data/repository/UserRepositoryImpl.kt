package com.akcrba.smartstep.feature.myprofile.data.repository

import com.akcrba.smartstep.feature.myprofile.data.local.dao.UserDao
import com.akcrba.smartstep.feature.myprofile.data.local.entity.toEntity
import com.akcrba.smartstep.feature.myprofile.domain.model.User
import com.akcrba.smartstep.feature.myprofile.domain.reposiroty.UserRepository
import org.koin.core.annotation.Singleton

@Singleton
internal class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

}
