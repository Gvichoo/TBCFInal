package com.tbacademy.nextstep.di

import androidx.work.WorkerFactory
import com.tbacademy.nextstep.App
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @Singleton
    abstract fun bindWorkerFactory(factory: App.MyWorkerFactory): WorkerFactory
}