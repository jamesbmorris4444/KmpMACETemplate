package com.vdigital.volumestream

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vdigital.volumestream.MediaPlayerContext.koinApplication
import com.vdigital.volumestream.di.viewModelModule
import com.vdigital.volumestream.platform.di.platformCoreModule
import com.vditital.data.di.externalDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication
import org.koin.dsl.module

internal object MediaPlayerContext {
    lateinit var koinApplication: KoinApplication
}

lateinit var instance: Application

class InitKoinContentProvider : ContentProvider() {
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int { return 0 }

    override fun getType(p0: Uri): String? { return "" }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? { return null }

    override fun onCreate(): Boolean {
        koinApplication = koinApplication {
            context?.let {
                androidContext(it.applicationContext)
                instance = it as Application
            }
        }
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? { return null }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int { return 0 }
}