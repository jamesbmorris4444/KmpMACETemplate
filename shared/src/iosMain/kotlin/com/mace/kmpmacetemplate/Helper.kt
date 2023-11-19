package com.mace.kmpmacetemplate

import org.koin.core.context.startKoin
import io.sentry.kotlin.multiplatform.Sentry

fun initKoin() {
    startKoin {
        modules(appModule())
    }
    Sentry.init {
        it.dsn = "https://examplePublicKey@o0.ingest.sentry.io/0"
    }
}