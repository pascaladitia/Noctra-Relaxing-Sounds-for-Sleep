package com.pascal.noctra.di

import com.pascal.noctra.data.audio.AudioPlayerManager
import com.pascal.noctra.data.repository.preset.PresetRepository
import com.pascal.noctra.data.repository.preset.PresetRepositoryImpl
import com.pascal.noctra.data.repository.settings.SettingsRepository
import com.pascal.noctra.data.repository.settings.SettingsRepositoryImpl
import com.pascal.noctra.data.repository.sound.SoundRepository
import com.pascal.noctra.data.repository.sound.SoundRepositoryImpl
import com.pascal.noctra.data.repository.auth.AuthRepository
import com.pascal.noctra.data.repository.auth.AuthRepositoryImpl
import com.pascal.noctra.data.repository.base.BaseRepository
import com.pascal.noctra.data.repository.base.BaseRepositoryImpl
import com.pascal.noctra.data.local.repository.profile.LocalProfileRepository
import com.pascal.noctra.data.local.repository.profile.LocalProfileRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single { com.pascal.noctra.createSettings() }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::BaseRepositoryImpl) { bind<BaseRepository>() }
    singleOf(::LocalProfileRepositoryImpl) { bind<LocalProfileRepository>() }
    singleOf(::SoundRepositoryImpl) { bind<SoundRepository>() }
    singleOf(::PresetRepositoryImpl) { bind<PresetRepository>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    single { AudioPlayerManager(get()) }
}
