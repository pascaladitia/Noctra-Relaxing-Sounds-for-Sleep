package com.pascal.noctra.di

import com.pascal.noctra.domain.usecase.local.LocalProfileUseCase
import com.pascal.noctra.domain.usecase.local.LocalProfileUseCaseImpl
import com.pascal.noctra.domain.usecase.local.sound.SoundUseCase
import com.pascal.noctra.domain.usecase.local.sound.SoundUseCaseImpl
import com.pascal.noctra.domain.usecase.local.preset.PresetUseCase
import com.pascal.noctra.domain.usecase.local.preset.PresetUseCaseImpl
import com.pascal.noctra.domain.usecase.local.settings.SettingsUseCase
import com.pascal.noctra.domain.usecase.local.settings.SettingsUseCaseImpl
import com.pascal.noctra.domain.usecase.remote.auth.AuthUseCase
import com.pascal.noctra.domain.usecase.remote.auth.AuthUseCaseImpl
import com.pascal.noctra.domain.usecase.remote.base.BaseUseCase
import com.pascal.noctra.domain.usecase.remote.base.BaseUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::AuthUseCaseImpl) { bind<AuthUseCase>() }
    singleOf(::BaseUseCaseImpl) { bind<BaseUseCase>() }
    singleOf(::LocalProfileUseCaseImpl) { bind<LocalProfileUseCase>() }
    singleOf(::SoundUseCaseImpl) { bind<SoundUseCase>() }
    singleOf(::PresetUseCaseImpl) { bind<PresetUseCase>() }
    singleOf(::SettingsUseCaseImpl) { bind<SettingsUseCase>() }
}
