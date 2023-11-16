package com.jetbrains.handson.kmm.shared

import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi

class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        Logger.d("JIMX getAllLaunches1 called111 $forceReload")
        val cachedLaunches = api.getAllLaunches()
        Logger.d("JIMX getAllLaunches1 called    $forceReload")
        Logger.d("JIMX getAllLaunches2 called    $cachedLaunches")
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                //database.createLaunches(it)
                return it
            }

        }
    }
}