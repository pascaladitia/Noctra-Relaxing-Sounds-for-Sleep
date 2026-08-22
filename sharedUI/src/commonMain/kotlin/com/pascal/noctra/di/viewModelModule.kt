package com.pascal.noctra.di

import com.pascal.noctra.ui.screen.home.HomeViewModel
import com.pascal.noctra.ui.screen.mixer.MixerViewModel
import com.pascal.noctra.ui.screen.onboarding.OnboardingViewModel
import com.pascal.noctra.ui.screen.settings.SettingsViewModel
import com.pascal.noctra.ui.screen.login.LoginViewModel
import com.pascal.noctra.ui.screen.profile.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf(::LoginViewModel)
    singleOf(::HomeViewModel)
    singleOf(::ProfileViewModel)
    singleOf(::OnboardingViewModel)
    singleOf(::MixerViewModel)
    singleOf(::SettingsViewModel)
}
