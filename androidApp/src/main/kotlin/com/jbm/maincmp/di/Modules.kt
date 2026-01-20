package com.mace.kmpmacetemplate.di

import com.jetbrains.handson.kmm.shared.SpaceXSDK
import org.koin.dsl.module
import viewstate.Repository
import viewstate.RepositoryImpl

fun androidAppModule() = module {
    single<Repository> { RepositoryImpl() }
    single { SpaceXSDK() }
}