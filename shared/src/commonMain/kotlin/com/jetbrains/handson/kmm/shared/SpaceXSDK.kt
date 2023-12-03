package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.entity.Movies
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi

class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = api.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                return it
            }

        }
    }

    @Throws(Exception::class) suspend fun getMovies(forceReload: Boolean): List<Movies> {
        val cachedMovies = api.getMovies()
        return if (cachedMovies.isNotEmpty() && !forceReload) {
            cachedMovies
        } else {
            api.getMovies().also {
                return it
            }

        }
    }
}