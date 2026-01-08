package com.mace.kmpmacetemplate.di

import Repository
import RepositoryImpl
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import org.koin.dsl.module

fun androidAppModule() = module {
    single<Repository> { RepositoryImpl() }
    single { DatabaseDriverFactory(get()) }
    single { SpaceXSDK() }
}