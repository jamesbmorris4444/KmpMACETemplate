package di.modules

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import com.sqldb.DatabaseDriverFactory
import org.koin.dsl.module

fun androidModule(context: Context) = module {
    single { DatabaseDriverFactory(context) }

    single<SqlDriver> {
        get<DatabaseDriverFactory>().create()
    }
}