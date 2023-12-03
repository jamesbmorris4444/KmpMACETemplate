package ui
import BloodViewModel
import Strings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.Movies
import extraBlue
import extraGreen
import io.kamel.core.Resource
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.utils.CacheControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun MoviesScreen(
    navigator: Navigator,
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

    Logger.i("MACELOG: Compose: ${ScreenNames.RocketLaunch.name}")
    val composableScope = rememberCoroutineScope()

    // state variables
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val completed by viewModel.moviesCompletedState.collectAsState()
    val isInvalid by viewModel.moviesInvalidState.collectAsState()
    val failure by viewModel.moviesFailureState.collectAsState()

    when {
        isInvalid -> {
            composableScope.launch(Dispatchers.Main) {
                val pair = viewModel.getMovies(composableScope)
                viewModel.moviesCompletedState.value = true
                viewModel.moviesInvalidState.value = false
                if (pair.second.isEmpty()) { // success
                    viewModel.moviesAvailableState.value = pair.first
                } else { // failure
                    viewModel.moviesFailureState. value = pair.second
                }
                Logger.d("JIMX 1  ${pair.first}")
                Logger.d("JIMX 2  ${pair.second}")
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
                    viewModel.moviesFailureState.value = ""
                    viewModel.showStandardModalState.value = StandardModalArgs()
                }
            }
        }

        completed -> {
            MoviesHandler(
                navigator = navigator,
                configAppBar = configAppBar,
                viewModel = viewModel,
                title = title
            )
        }
    }
}

@Composable
fun MoviesHandler(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    // state variables
    val movies: List<Movies> by viewModel.moviesAvailableState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val posterPath = "https://image.tmdb.org/t/p/w500${movies[0].posterPath}" // or 185
    val painterResource: Resource<Painter> = asyncPainterResource(posterPath) {
        coroutineContext = coroutineScope.coroutineContext
        requestBuilder {
            header("Key", "Value")
            parameter("Key", "Value")
            cacheControl(CacheControl.MAX_AGE)
        }
    }
    KamelImage(
        modifier = Modifier.width(100.dp),
        resource = painterResource,
        contentDescription = null,
        onFailure = { exception ->
            coroutineScope.launch {
                Logger.d("JIMX  6   ${exception.message.toString()}")
            }
        }
    )

//    @Composable
//    fun LaunchesList(launches: List<RocketLaunch>) {
//        LazyColumn {
//            launches.forEachIndexed { index, _ ->
//                item {
//                    MoviesDisplay(
//                        launches[index].flightNumber.toString(),
//                        launches[index].missionName,
//                        launches[index].details ?: "",
//                        launches[index].launchDate,
//                        launches[index].launchSuccess ?: true
//                    )
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(key1 = true) {
//        configAppBar(
//            AppBarState(
//                title = title,
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navigator.navigate(
//                            route = ScreenNames.DonateProductsSearch.name,
//                            NavOptions(popUpTo = PopUpTo(ScreenNames.RocketLaunch.name, inclusive = true))
//                        )
//                    }) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = Strings.get("back_button_content_description")
//                        )
//                    }
//                }
//            )
//        )
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 24.dp, end = 24.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.TopCenter),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            LaunchesList(movies)
//        }
//    }
}

@Composable
fun MoviesDisplay(
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