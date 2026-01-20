package di.modules

import app.cash.sqldelight.db.SqlDriver
import com.sqldb.DatabaseDriverFactory
import org.koin.dsl.module

fun commonModule() = module {
//    // Declare a singleton for HttpClient using the platform-specific implementation
//    single { createHttpClient() }
//    // Declare a singleton for NewsApiService, injecting HttpClient
//    single<NewsSource> { NewsApiService(get()) }
//    // Declare a singleton for NewsRepository, injecting NewsApiService
//    single { NewsRepository(get(), get()) }
//    // Declare a singleton for GetNewsUseCase, injecting NewsRepository
//    single { GetNewsUseCase(get()) }
//    // Declare a factory for NewsViewModel, injecting GetNewsUseCase
//    factory { NewsViewModel(get()) }
//
//    // Provide MyDatabase
//    single {
//        NewsDatabase(get())
//    }
//    // Provide CacheManager
//    single<CacheManager> {
//        SqlDelightCacheManager(get())
//    }

    // Provide SqlDriver using DatabaseDriverFactory
    single<SqlDriver> { get<DatabaseDriverFactory>().create() }

}
