package com.pascal.noctra.di

import com.pascal.noctra.data.audio.AudioEngine
import com.pascal.noctra.data.audio.IosAudioEngine
import org.koin.dsl.module

val audioModule = module {
    single<AudioEngine> { IosAudioEngine() }
}
