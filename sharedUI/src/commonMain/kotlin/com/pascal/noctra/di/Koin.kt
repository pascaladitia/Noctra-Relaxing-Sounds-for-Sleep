package com.pascal.noctra.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    enableNetworkLogs: Boolean = true,
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    modules(
        databaseModule,
        firebaseModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        *platformModules.toTypedArray()
    )
    appDeclaration()
}

fun KoinApplication.Companion.start(): KoinApplication = initKoin { }
