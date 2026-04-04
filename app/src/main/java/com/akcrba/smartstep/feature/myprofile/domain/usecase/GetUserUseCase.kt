package com.akcrba.smartstep.feature.myprofile.domain.usecase

import com.akcrba.smartstep.feature.myprofile.domain.model.User
import org.koin.core.annotation.Singleton

// @Singleton(binds = [GetUserUseCaseImpl::class])
internal interface GetUserUseCase {
    suspend operator fun invoke(user: User): Unit // Result<Unit, Error>
}

@Singleton
internal class GetUserUseCaseImpl : GetUserUseCase {
    override suspend fun invoke(user: User): Unit = println("User was retrieved")
}
