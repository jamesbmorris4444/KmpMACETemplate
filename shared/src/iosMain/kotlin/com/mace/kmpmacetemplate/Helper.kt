package com.mace.kmpmacetemplate

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule())
    }
}