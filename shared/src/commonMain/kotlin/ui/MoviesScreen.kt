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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.Movie
import io.kamel.core.Resource
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.utils.CacheControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@Composable
fun MoviesScreen(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    Logger.i("MACELOG: Compose: ${ScreenNames.RocketLaunch.name}")
    MoviesHandler(
        navigator = navigator,
        configAppBar = configAppBar,
        viewModel = viewModel,
        title = title
    )
}

@Composable
fun MoviesHandler(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    val coroutineScope = rememberCoroutineScope()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val genreMap: Map<Int, String> = mapOf(Pair(37, "Western"), Pair(10759, "Action & Adventure"), Pair (80, "Crime"), Pair(18, "Drama"), Pair(9648, "Mystery"))

    @Composable
    fun standardModalError(failureMessage: String) {
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
                    bodyText = failureMessage,
                    positiveText = Strings.get("positive_button_text_ok"),
                ) {
                    viewModel.showStandardModalState.value = StandardModalArgs()
                }
            }
    }

    @Composable
    fun LaunchesList() {
        val movies: LazyPagingItems<Movie> = viewModel.moviesAvailableState.collectAsLazyPagingItems()
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn {
            items(count = movies.itemCount) { index ->
                val genres = movies[index]?.genreIds?.filter { genreMap[it].isNullOrEmpty().not() }
                MoviesDisplay(
                    title = movies[index]?.title ?: "",
                    posterPath = movies[index]?.posterPath ?: "",
                    genre = genres?.map { genreMap[it] ?: "" } ?: listOf(),
                    voteAverage = movies[index]?.voteAverage ?: 0.0f,
                    popularity = movies[index]?.popularity?.toInt() ?: 0,
                    coroutineScope = coroutineScope
                )
            }
            movies.apply {
                when {
                    loadState.refresh is LoadStateError -> {
                        item {
                            (movies.loadState.refresh as LoadStateError).error.message?.let {
                                standardModalError(it)
                            }
                        }
                    }
                    loadState.append is LoadStateError -> {
                        item {
                            (movies.loadState.append as LoadStateError).error.message?.let {
                                standardModalError(it)
                            }
                        }
                    }
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
                            NavOptions(popUpTo = PopUpTo(ScreenNames.Movies.name, inclusive = true))
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
            LaunchesList()
        }
    }
}

@Composable
fun MoviesDisplay(
    title: String,
    genre: List<String>,
    voteAverage: Float,
    popularity: Int,
    posterPath: String,
    coroutineScope: CoroutineScope
) {
    ListDisplayText("item_title", Strings.get("title"), title)
    ListDisplayText("item_genre", Strings.get("genre"), genre.toString())
    ListDisplayText("item_vote_average", Strings.get("vote_average"), voteAverage.toString())
    ListDisplayText("item_popularity", Strings.get("popularity"), popularity.toString())
    val fullPath = "https://image.tmdb.org/t/p/w500$posterPath" // or 185
    val painterResource: Resource<Painter> = asyncPainterResource(fullPath) {
        coroutineContext = coroutineScope.coroutineContext
        requestBuilder {
            header("Key", "Value")
            parameter("Key", "Value")
            cacheControl(CacheControl.MAX_AGE)
        }
    }
    KamelImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
            .width(240.dp),
        resource = painterResource,
        contentDescription = null,
        onFailure = { exception ->
            coroutineScope.launch {
                Logger.d("Kamel EXCEPTION=${exception.message.toString()}")
            }
        }
    )
    Divider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = MaterialTheme.colors.onBackground, thickness = 2.dp)
}