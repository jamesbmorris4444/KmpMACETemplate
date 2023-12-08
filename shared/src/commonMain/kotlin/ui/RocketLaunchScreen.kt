package ui
import BloodViewModel
import Strings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@Composable
fun RocketLaunchScreen(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {

    @Composable
    fun CustomCircularProgressBar() {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colors.primary,
            strokeWidth = 6.dp
        )
    }

    Logger.i("MACELOG: Compose: ${ScreenNames.RocketLaunch.name}")
    val composableScope = rememberCoroutineScope()

    // state variables
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val completed by viewModel.refreshCompletedState.collectAsState()
    val isInvalid by viewModel.rocketLaunchesInvalidState.collectAsState()
    val failure by viewModel.refreshFailureState.collectAsState()

    when {
        isInvalid -> {
            composableScope.launch(Dispatchers.Main) {
                val pair = viewModel.getSpaceXLaunches(composableScope)
                viewModel.refreshCompletedState.value = true
                viewModel.rocketLaunchesInvalidState.value = false
                if (pair.second.isEmpty()) { // success
                    viewModel.launchesAvailableState.value = pair.first
                } else { // failure
                    viewModel.refreshFailureState. value = pair.second
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomCircularProgressBar()
            }
        }

        failure.isNotEmpty() -> {
            if (showStandardModalState.topIconId.isNotEmpty()) {
                StandardModal(
                    showStandardModalState.topIconId,
                    showStandardModalState.titleText,
                    showStandardModalState.bodyText,
                    showStandardModalState.positiveText,
                    showStandardModalState.negativeText,
                    showStandardModalState.neutralText,
                    showStandardModalState.onDismiss
                )
            } else {
                viewModel.showStandardModalState.value = StandardModalArgs(
                    topIconId = "drawable/notification.xml",
                    titleText = Strings.get("failure_api_title_text"),
                    bodyText = failure,
                    positiveText = Strings.get("positive_button_text_ok"),
                ) {
                    viewModel.refreshFailureState.value = ""
                    viewModel.showStandardModalState.value = StandardModalArgs()
                }
            }
        }

        completed -> {
            RocketLaunchHandler(
                navigator = navigator,
                configAppBar = configAppBar,
                viewModel = viewModel,
                title = title
            )
        }
    }
}

@Composable
fun RocketLaunchHandler(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    // state variables
    val launches: List<RocketLaunch> by viewModel.launchesAvailableState.collectAsState()

    @Composable
    fun LaunchesList(launches: List<RocketLaunch>) {
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn {
            launches.forEachIndexed { index, _ ->
                item {
                    LaunchElementText(
                        launches[index].flightNumber.toString(),
                        launches[index].missionName,
                        launches[index].details ?: "",
                        launches[index].launchDate,
                        launches[index].launchSuccess ?: true
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        configAppBar(
            AppBarState(
                title = title,
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigate(
                            route = ScreenNames.Movies.name,
                            NavOptions(popUpTo = PopUpTo(ScreenNames.RocketLaunch.name, inclusive = true))
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = Strings.get("back_button_content_description")
                        )
                    }
                }
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchesList(launches)
        }
    }
}

@Composable
fun LaunchElementText(
    flightNumber: String,
    missionName: String,
    details: String,
    launchDate: String,
    launchSuccess: Boolean
) {
    ListDisplayText("item_flight_number", Strings.get("flight_number"), flightNumber)
    ListDisplayText("item_mission_name", Strings.get("mission_name"), missionName)
    ListDisplayText("item_details", Strings.get("details"), details)
    ListDisplayText("item_launch_date", Strings.get("launch_date"), launchDate)
    ListDisplayText(
        "item_mission_outcome",
        Strings.get("mission_outcome"),
        if (launchSuccess) Strings.get("successful") else Strings.get("failed"),
        if (launchSuccess) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.error
    )
    Divider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = MaterialTheme.colors.onBackground, thickness = 2.dp)
}