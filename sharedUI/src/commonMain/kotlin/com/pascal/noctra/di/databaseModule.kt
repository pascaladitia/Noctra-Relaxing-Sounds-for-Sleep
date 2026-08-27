package com.pascal.noctra.di

import androidx.room.RoomDatabase
import com.pascal.noctra.data.local.database.AppDatabase
import com.pascal.noctra.data.local.database.getRoomDatabase
import com.pascal.noctra.getDatabaseBuilder
import org.koin.dsl.module

val databaseModule = module {

    single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder() }
    single<AppDatabase> { getRoomDatabase(get()) }
}
