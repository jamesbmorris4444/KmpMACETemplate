package com.mace.kmpmacetemplate

import org.koin.core.context.startKoin

object Helper {
    fun initKoin() {
        startKoin {
            modules(appModule())
        }.koin

    }
}