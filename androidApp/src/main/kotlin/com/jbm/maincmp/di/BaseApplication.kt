package com.mace.kmpmacetemplate.di

import android.app.Application
import com.vdigital.volumestream.InitKoinContentProvider
import com.vdigital.volumestream.di.viewModelModule
import com.vdigital.volumestream.platform.di.platformCoreModule
import com.vditital.data.di.externalDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        InitKoinContentProvider()
        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(androidAppModule() + externalDataModule + viewModelModule + platformCoreModule)
        }
    }
}