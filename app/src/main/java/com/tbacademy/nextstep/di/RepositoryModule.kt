package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.data.repository.login.LoginRepositoryImpl
import com.tbacademy.nextstep.data.repository.register.RegisterRepositoryImpl
import com.tbacademy.nextstep.data.repository.userSession.UserSessionManagerRepositoryImpl
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindRegisterRepository(impl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    abstract fun bindUserSessionManagerRepository(impl : UserSessionManagerRepositoryImpl) : UserSessionManagerRepository
}