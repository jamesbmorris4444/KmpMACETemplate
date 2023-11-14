package com.mace.kmpmacetemplate.android

import BloodViewModel
import MaceTemplateTheme
import RepositoryImpl
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import ui.RocketLaunchScreen


class MainActivity : ComponentActivity() {
    private val databaseDriverFactory = DatabaseDriverFactory(this)
    private val sdk = SpaceXSDK(databaseDriverFactory)
    private val repository = RepositoryImpl(sdk, databaseDriverFactory)
    private val viewModel: BloodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Strings.context = this
        repository.screenWidth = convertPixelsToDp(Resources.getSystem().displayMetrics.widthPixels.toFloat(), this).toInt()
        repository.screenHeight = convertPixelsToDp(Resources.getSystem().displayMetrics.heightPixels.toFloat(), this).toInt()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            MaceTemplateTheme {
                RocketLaunchScreen(
                    repository = repository,
                    configAppBar = { },
                    viewModel = viewModel,
                    title = "SpaceX Rocket Launches"
                )
            }
        }
    }

    private fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onDestroy() {
        super.onDestroy()
        //repository.saveStagingDatabase(Constants.MODIFIED_DATABASE_NAME, getDatabasePath(Constants.MODIFIED_DATABASE_NAME))
    }
}