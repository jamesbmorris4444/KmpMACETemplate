package viewstate

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch

data class RocketViewState (
    var launchesAvailable: List<RocketLaunch>? = null,
    var launchesFailure: String = "",
    val progressBarState: Boolean = false
)