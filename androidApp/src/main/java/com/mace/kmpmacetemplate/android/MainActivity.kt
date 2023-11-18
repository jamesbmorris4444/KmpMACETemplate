package com.mace.kmpmacetemplate.android

import BloodViewModel
import RepositoryImpl
import StartApplication
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    private val repository = RepositoryImpl()
    private val viewModel: BloodViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Strings.context = this
        repository.screenWidth = convertPixelsToDp(Resources.getSystem().displayMetrics.widthPixels.toFloat(), this).toInt()
        repository.screenHeight = convertPixelsToDp(Resources.getSystem().displayMetrics.heightPixels.toFloat(), this).toInt()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            StartApplication(viewModel, repository)
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