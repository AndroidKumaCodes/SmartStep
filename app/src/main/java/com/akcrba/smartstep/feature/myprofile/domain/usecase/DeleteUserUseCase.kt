package com.akcrba.smartstep.feature.myprofile.domain.usecase

import com.akcrba.smartstep.feature.myprofile.domain.reposiroty.UserRepository
import org.koin.core.annotation.Singleton

internal interface DeleteUserUseCase {
    suspend operator fun invoke() // Result<Unit, Error>
}

@Singleton
internal class DeleteUserUseCaseImpl(
    private val userRepository: UserRepository,
) : DeleteUserUseCase {
    override suspend fun invoke() {
        userRepository.deleteUser()
        println("User deleted")
    }
}
