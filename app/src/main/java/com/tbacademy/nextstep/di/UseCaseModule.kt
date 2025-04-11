package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateEmailUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateEmailUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateNecessaryFieldUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateNecessaryFieldUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidatePasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidatePasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateRepeatedPasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateRepeatedPasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateUsernameUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateUsernameUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCaseImpl
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

    @Singleton
    @Binds
    fun bindValidateAddGoalTitleUseCase(impl : ValidateAddGoalTitleUseCaseImpl) : ValidateAddGoalTitleUseCase

    @Singleton
    @Binds
    fun bindValidateAddGoalDescriptionUseCase(impl : ValidateAddGoalDescriptionUseCaseImpl) : ValidateAddGoalDescriptionUseCase

    @Singleton
    @Binds
    fun bindValidateAddGoalDateUseCase(impl : ValidateAddGoalDateUseCaseImpl) : ValidateAddGoalDateUseCase

    @Singleton
    @Binds
    fun bindValidateMetricTargetUseCase(impl : ValidateMetricTargetUseCaseImpl) : ValidateMetricTargetUseCase

    @Singleton
    @Binds
    fun bindValidateMetricUnitUseCase(impl : ValidateMetricUnitUseCaseImpl) : ValidateMetricUnitUseCase

    @Singleton
    @Binds
    fun bindGetPostsUseCase(impl : GetPostsUseCaseImpl) : GetPostsUseCase
}