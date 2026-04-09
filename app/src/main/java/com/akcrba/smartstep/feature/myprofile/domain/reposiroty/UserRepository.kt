package com.akcrba.smartstep.feature.myprofile.domain.reposiroty

import com.akcrba.smartstep.feature.myprofile.domain.model.User

internal interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
}
