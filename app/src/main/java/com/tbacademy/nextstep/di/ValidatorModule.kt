package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDateValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDateValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDescriptionValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalDescriptionValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalTitleValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.AddGoalTitleValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricTargetValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricTargetValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricUnitValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.MetricUnitValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.EmailValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.NecessaryFieldValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.NecessaryFieldValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.PasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.PasswordValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.RepeatedPasswordValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.RepeatedPasswordValidatorImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.UsernameValidator
import com.tbacademy.nextstep.domain.usecase.validation.authorization.UsernameValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ValidatorModule{
    @Singleton
    @Binds
    fun bindEmailValidator(validateEmailUseCase: EmailValidatorImpl): EmailValidator

    @Singleton
    @Binds
    fun bindPasswordValidator(validatePasswordUseCase: PasswordValidatorImpl): PasswordValidator

    @Singleton
    @Binds
    fun bindRepeatedPasswordValidator(validateRepeatedPasswordUseCase: RepeatedPasswordValidatorImpl): RepeatedPasswordValidator

    @Singleton
    @Binds
    fun bindUsernameValidator(validateUsernameUseCase: UsernameValidatorImpl): UsernameValidator

    @Singleton
    @Binds
    fun bindNecessaryFieldValidator(validateNecessaryFieldUseCase: NecessaryFieldValidatorImpl): NecessaryFieldValidator

    @Singleton
    @Binds
    fun bindAddGoalTitleValidator(impl : AddGoalTitleValidatorImpl) : AddGoalTitleValidator

    @Singleton
    @Binds
    fun bindAddGoalDescriptionValidator(impl : AddGoalDescriptionValidatorImpl) : AddGoalDescriptionValidator

    @Singleton
    @Binds
    fun bindAddGoalDateValidator(impl : AddGoalDateValidatorImpl) : AddGoalDateValidator

    @Singleton
    @Binds
    fun bindMetricTargetValidator(impl : MetricTargetValidatorImpl) : MetricTargetValidator

    @Singleton
    @Binds
    fun bindMetricUnitValidator(impl : MetricUnitValidatorImpl) : MetricUnitValidator


}