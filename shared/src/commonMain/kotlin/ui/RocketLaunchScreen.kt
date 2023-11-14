package ui
import BloodViewModel
import Repository
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import extraBlue
import extraGreen
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle

@Composable
fun RocketLaunchScreen(
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
//    viewModel.setAppDatabase()
//    val showStandardModalState = viewModel.showStandardModalState.observeAsState().value ?: StandardModalArgs()
    val composableScope = rememberCoroutineScope()
    val completed = viewModel.refreshCompletedState.collectAsStateWithLifecycle().value
    val isInvalid = viewModel.databaseInvalidState.collectAsStateWithLifecycle().value
    val failure = viewModel.refreshFailureState.collectAsStateWithLifecycle().value
    when {
        isInvalid -> {
            composableScope.launch {
                val pair = repository.refreshDatabase(composableScope)
                if (pair.second.isEmpty()) {
                    viewModel.updateRefreshCompletedState(true)
                    viewModel.updateDatabaseInvalidState(false)
                    viewModel.updateRefreshFailureState("")
                    viewModel.updateLaunchesAvailableState(pair.first)
                } else {
                    viewModel.updateRefreshCompletedState(true)
                    viewModel.updateDatabaseInvalidState(false)
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
//            if (showStandardModalState.topIconResId >= 0) {
//                StandardModal(
//                    showStandardModalState.topIconResId,
//                    showStandardModalState.titleText,
//                    showStandardModalState.bodyText,
//                    showStandardModalState.positiveText,
//                    showStandardModalState.negativeText,
//                    showStandardModalState.neutralText,
//                    showStandardModalState.onDismiss
//                )
//            } else {
//                viewModel.changeShowStandardModalState(
//                    StandardModalArgs(
//                        topIconResId = R.drawable.notification,
//                        titleText = viewModel.getResources().getString(R.string.failure_db_entries_title_text),
//                        bodyText = viewModel.getResources().getString(R.string.failure_db_entries_body_text, failure),
//                        positiveText = viewModel.getResources().getString(R.string.positive_button_text_ok),
//                    ) {
//                        navigateUp()
//                        viewModel.changeShowStandardModalState(StandardModalArgs())
//                        viewModel.changeRefreshFailureState("")
//                    }
//                )
//            }
        }

        completed -> {
            RocketLaunchHandler(
                configAppBar = configAppBar,
                viewModel = viewModel,
                title = title)
        }

        else -> {
            viewModel.updateRefreshCompletedState(false)
            viewModel.updateDatabaseInvalidState(true)
            viewModel.updateRefreshFailureState("")
        }
    }
}

@Composable
fun RocketLaunchHandler(
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String,
) {
    val launches: List<RocketLaunch> = viewModel.launchesAvailableState.collectAsStateWithLifecycle().value

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
                title = title
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
