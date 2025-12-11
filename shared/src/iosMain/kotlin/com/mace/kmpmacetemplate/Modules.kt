package com.mace.kmpmacetemplate

import Repository
import RepositoryImpl
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.vdigital.volumestream.di.viewModelModule
import com.vdigital.volumestream.platform.di.platformCoreModule
import com.vditital.data.di.externalDataModule
import org.koin.dsl.module

fun iosAppModule() = module {
    single<Repository> { RepositoryImpl() }
    single { DatabaseDriverFactory() }
    single { SpaceXSDK() }
} + externalDataModule + viewModelModule + platformCoreModule