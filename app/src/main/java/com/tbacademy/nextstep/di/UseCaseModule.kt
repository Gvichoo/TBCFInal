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
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDateValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDateValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDescriptionValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDescriptionValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.NecessaryFieldValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.PasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.RepeatedPasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.UsernameValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalTitleValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalTitleValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricTargetValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricTargetValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricUnitValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricUnitValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.NecessaryFieldValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.PasswordValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.RepeatedPasswordValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.UsernameValidatorImpl
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
    fun bindGetPostsUseCase(impl: GetPostsUseCaseImpl): GetPostsUseCase

}