package com.akcrba.smartstep.feature.myprofile.domain.usecase

import com.akcrba.smartstep.feature.myprofile.domain.model.User
import com.akcrba.smartstep.feature.myprofile.domain.reposiroty.UserRepository
import org.koin.core.annotation.Singleton

internal interface SaveUserUseCase {
    suspend operator fun invoke(user: User) // Result<Unit, Error>
}

@Singleton
internal class SaveUserUseCaseImpl(
    private val userRepository: UserRepository,
) : SaveUserUseCase {
    override suspend fun invoke(user: User) {
        userRepository.saveUser(user)
        println("User saved: $user")
    }
}
