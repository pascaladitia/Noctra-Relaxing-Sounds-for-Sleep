package com.pascal.noctra.di

import com.pascal.noctra.ContextUtils
import com.pascal.noctra.data.audio.AndroidAudioEngine
import com.pascal.noctra.data.audio.AudioEngine
import org.koin.dsl.module

val audioModule = module {
    single<AudioEngine> {
        AndroidAudioEngine(context = ContextUtils.context)
    }
}
