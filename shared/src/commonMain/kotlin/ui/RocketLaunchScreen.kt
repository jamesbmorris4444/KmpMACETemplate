package ui
import BloodViewModel
import Strings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.mace.corelib.StandardModal
import com.mace.corelib.StandardModalArgs
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import progressBar

@Composable
fun RocketLaunchScreen(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {

    @Composable
    fun handleFailure(viewModel: BloodViewModel, message: String, typeOfApi: ApiCalls, showStandardModalState: StandardModalArgs) {
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
                titleText = Strings.format("failure_api_title_text", typeOfApi.string),
                bodyText = message,
                positiveText = Strings.get("positive_button_text_ok"),
            ) {
                viewModel.launchesFailure.value = ""
                viewModel.launchesAvailable.value = listOf()
                viewModel.progressBarState.value = false
                viewModel.showStandardModalState.value = StandardModalArgs()
            }
        }
    }

    val launchesAvailable: List<RocketLaunch>? by viewModel.launchesAvailable.collectAsState()
    val launchesFailure by viewModel.launchesFailure.collectAsState()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    Logger.i("MACELOG: Compose: ${ScreenNames.RocketLaunch.name}")
    when {
        launchesFailure.isNotEmpty() -> handleFailure(viewModel, launchesFailure, ApiCalls.SpaceX, showStandardModalState)
        launchesAvailable != null -> launchesAvailable ?.let { RocketLaunchHandler(navigator = navigator, configAppBar = configAppBar, title = title, launches = it) }
        else -> {
            progressBar()
            genericApiCall(apiType = ApiCalls.SpaceX, viewModel = viewModel)
        }
    }
}

@Composable
fun RocketLaunchHandler(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    title: String,
    launches: List<RocketLaunch>
) {
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