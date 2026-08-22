package com.pascal.noctra.di

import com.pascal.noctra.data.remote.KtorClientFactory
import com.pascal.noctra.data.remote.api.BaseApi
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        KtorClientFactory.create()
    }
    singleOf(::BaseApi)
}
