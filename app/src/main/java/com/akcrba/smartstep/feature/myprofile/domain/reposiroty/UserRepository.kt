package com.akcrba.smartstep.feature.myprofile.domain.reposiroty

import com.akcrba.smartstep.feature.myprofile.domain.model.User
import kotlinx.coroutines.flow.Flow

internal interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
}
