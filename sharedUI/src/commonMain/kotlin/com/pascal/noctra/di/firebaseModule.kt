package com.pascal.noctra.di

import com.pascal.noctra.data.firebase.FirebaseAuthClient
import com.pascal.noctra.data.firebase.FirebaseFirestoreClient
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseAuthClient() }
    single { FirebaseFirestoreClient() }
}
