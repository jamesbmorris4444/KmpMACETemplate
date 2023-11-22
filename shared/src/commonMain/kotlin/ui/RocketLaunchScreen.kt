package ui
import BloodViewModel
import Repository
import Strings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import extraBlue
import extraGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@Composable
fun RocketLaunchScreen(
    navigator: Navigator,
    repository: Repository,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {

    @Composable
    fun CustomCircularProgressBar() {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colors.extraBlue,
            strokeWidth = 6.dp
        )
    }

    Logger.d("MACELOG: Compose: ${ScreenNames.RocketLaunch.name}")
    val composableScope = rememberCoroutineScope()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val completed by viewModel.refreshCompletedState.collectAsState()
    val isInvalid by viewModel.rocketLaunchesInvalidState.collectAsState()
    val failure by viewModel.refreshFailureState.collectAsState()
    Logger.d("JIMX 1  $showStandardModalState   $completed  $isInvalid   $failure")
    when {
        isInvalid -> {
            composableScope.launch(Dispatchers.Main) {
                val pair = repository.refreshDatabase(composableScope)
                Logger.d("JIMX 2 ")
                viewModel.updateRefreshCompletedState(true)
                viewModel.updateRocketLaunchesInvalidState(false)
                if (pair.second.isEmpty()) { // success
                    viewModel.updateLaunchesAvailableState(pair.first)
                } else { // failure
                    viewModel.updateRefreshFailureState(pair.second)
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
                viewModel.changeShowStandardModalState(
                    StandardModalArgs(
                        topIconId = "drawable/notification.xml",
                        titleText = Strings.get("failure_db_entries_title_text"),
                        bodyText = Strings.format("failure_db_entries_body_text", failure),
                        positiveText = Strings.get("positive_button_text_ok"),
                    ) {
                        viewModel.updateRefreshFailureState("")
                    }
                )
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
    val launches: List<RocketLaunch> by viewModel.launchesAvailableState.collectAsState()

    @Composable
    fun LaunchesList(launches: List<RocketLaunch>) {
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
                            route = ScreenNames.DonateProductsSearch.name,
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
    Text(
        modifier = Modifier.testTag("item"),
        text = "flight number: $flightNumber",
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.body1
    )
    Text(
        modifier = Modifier.testTag("item"),
        text = "mission name: $missionName",
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.body1
    )
    Text(
        modifier = Modifier.testTag("item"),
        text = "details: $details",
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.body1
    )
    Text(
        modifier = Modifier.testTag("item"),
        text = "launch date: $launchDate",
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.body1
    )
    Text(
        modifier = Modifier.testTag("item"),
        text = if (launchSuccess) "Successful" else "Failed",
        color = if (launchSuccess) MaterialTheme.colors.extraGreen else MaterialTheme.colors.error,
        style = MaterialTheme.typography.body1
    )
    Divider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = MaterialTheme.colors.onBackground, thickness = 2.dp)
}
