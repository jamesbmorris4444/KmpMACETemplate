package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi

class SpaceXSDK {
    @Throws(Exception::class) suspend fun getLaunches(): List<RocketLaunch> = SpaceXApi().getAllLaunches()
}