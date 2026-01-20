package di.modules

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    // Execute any additional app-specific declarations
    appDeclaration()
    // Load common Koin modules
    modules(commonModule())
}

fun initKoin() = initKoin {}