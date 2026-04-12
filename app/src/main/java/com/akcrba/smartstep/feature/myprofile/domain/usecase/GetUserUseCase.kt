package com.akcrba.smartstep.feature.myprofile.domain.usecase

import com.akcrba.smartstep.feature.myprofile.domain.model.User
import com.akcrba.smartstep.feature.myprofile.domain.reposiroty.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Singleton

internal interface GetUserUseCase {
    operator fun invoke(): Flow<User?> // Result<Unit, Error>
}

@Singleton
internal class GetUserUseCaseImpl(private val userRepository: UserRepository) : GetUserUseCase {
    override fun invoke(): Flow<User?> = userRepository.getUser().onEach { user ->
        println("User was retrieved: $user")
    }
}
