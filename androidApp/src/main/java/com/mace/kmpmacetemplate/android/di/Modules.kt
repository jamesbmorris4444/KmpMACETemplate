package com.mace.kmpmacetemplate.android.di

import Repository
import RepositoryImpl
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import org.koin.dsl.module

fun appModule() = module {
    single<Repository> { RepositoryImpl() }
    single { DatabaseDriverFactory(get()) }
    single { SpaceXSDK(get()) }
}