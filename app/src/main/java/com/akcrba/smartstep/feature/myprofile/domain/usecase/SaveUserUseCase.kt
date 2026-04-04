package com.akcrba.smartstep.feature.myprofile.domain.usecase

import com.akcrba.smartstep.feature.myprofile.domain.model.User
import org.koin.core.annotation.Singleton

// @Singleton(binds = [SaveUserUseCaseImpl::class])
internal interface SaveUserUseCase {
    suspend operator fun invoke(user: User) // Result<Unit, Error>
}

@Singleton
internal class SaveUserUseCaseImpl : SaveUserUseCase {
    override suspend fun invoke(user: User): Unit = println("User was saved")
}
