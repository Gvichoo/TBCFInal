package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidateEmailUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateEmailUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidateNecessaryFieldUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateNecessaryFieldUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidatePasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidatePasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidateRepeatedPasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateRepeatedPasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.ValidateUsernameUseCase
import com.tbacademy.nextstep.domain.usecase.validation.ValidateUsernameUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Singleton
    @Binds
    fun bindValidateEmailUseCase(validateEmailUseCase: ValidateEmailUseCaseImpl): ValidateEmailUseCase

    @Singleton
    @Binds
    fun bindValidatePasswordUseCase(validatePasswordUseCase: ValidatePasswordUseCaseImpl): ValidatePasswordUseCase

    @Singleton
    @Binds
    fun bindValidateRepeatedPasswordUseCase(validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCaseImpl): ValidateRepeatedPasswordUseCase

    @Singleton
    @Binds
    fun bindValidateUsernameUseCase(validateUsernameUseCase: ValidateUsernameUseCaseImpl): ValidateUsernameUseCase

    @Singleton
    @Binds
    fun bindNecessaryFieldUseCase(validateNecessaryFieldUseCase: ValidateNecessaryFieldUseCaseImpl): ValidateNecessaryFieldUseCase

    @Singleton
    @Binds
    fun bindGetValueFromLocalStorageUseCase(impl: ReadValueFromLocalStorageUseCaseImpl) : ReadValueFromLocalStorageUseCase

    @Singleton
    @Binds
    fun bindSaveValueToLocalStorageUseCase(impl : SaveValueToLocalStorageUseCaseImpl) : SaveValueToLocalStorageUseCase

    @Singleton
    @Binds
    fun bindClearValueFromLocalStorageUseCase(impl : ClearValueFromLocalStorageUseCaseImpl) : ClearValueFromLocalStorageUseCase

    @Singleton
    @Binds
    fun bindCreateGoalUseCase(impl : CreateGoalUseCaseImpl) : CreateGoalUseCase

}