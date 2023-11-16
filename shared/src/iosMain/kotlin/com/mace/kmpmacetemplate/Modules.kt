package com.mace.kmpmacetemplate

import Repository
import RepositoryImpl
import org.koin.dsl.module

fun appModule() = module {
    single<Repository> { RepositoryImpl(get(), get()) }
}