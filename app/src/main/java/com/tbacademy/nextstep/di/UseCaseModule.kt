package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.domain.usecase.validation.EmailValidationUseCase
import com.tbacademy.nextstep.domain.usecase.validation.NicknameValidationUseCase
import com.tbacademy.nextstep.domain.usecase.validation.PasswordValidationUseCase
import com.tbacademy.nextstep.domain.usecase.validation.RepeatedPasswordValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideEmailValidationUseCase(): EmailValidationUseCase {
        return EmailValidationUseCase()
    }

    @Provides
    fun providePasswordValidationUseCase(): PasswordValidationUseCase {
        return PasswordValidationUseCase()
    }

    @Provides
    fun provideRepeatedPasswordValidationUseCase(): RepeatedPasswordValidationUseCase {
        return RepeatedPasswordValidationUseCase()
    }

    @Provides
    fun provideNicknameValidationUseCase(): NicknameValidationUseCase {
        return NicknameValidationUseCase()
    }

}