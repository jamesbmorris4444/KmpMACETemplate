package com.mace.kmpmacetemplate.di

import Repository
import RepositoryImpl
import org.koin.dsl.module

fun appModule() = module {
    single<Repository> { RepositoryImpl(get(), get()) }
}