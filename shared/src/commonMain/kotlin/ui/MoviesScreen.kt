package ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import co.touchlab.kermit.Logger
import com.Strings
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.mace.corelib.StandardModal
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
import viewmodels.AbstractMovieViewModel.MoviesIntent
import viewmodels.MovieViewModel
import viewstate.MoviesViewState

@Composable
fun MoviesScreen(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    viewModel: MovieViewModel,
    title: String
) {
    Logger.i("MACELOG: Compose: ${ScreenNames.Movies.name}")
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
    viewModel: MovieViewModel,
    title: String
) {
    val coroutineScope = rememberCoroutineScope()
    val moviesViewState: MoviesViewState by viewModel.moviesViewState.collectAsState()
    val genreMap: Map<Int, String> = mapOf(Pair(37, "Western"), Pair(10759, "Action & Adventure"), Pair (80, "Crime"), Pair(18, "Drama"), Pair(9648, "Mystery"))

    @Composable
    fun standardModalError(failureMessage: String) {
        StandardModal(
            topIconId = "drawable/notification.xml",
            titleText = Strings.get("failure_api_title_text"),
            bodyText = failureMessage,
            positiveText = Strings.get("positive_button_text_ok"),
        ) {
            viewModel.handleIntent(MoviesIntent.apiFailure(failureMessage))
        }
    }

    when {
        moviesViewState.apiFailure.isNotEmpty() -> standardModalError(moviesViewState.apiFailure)
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
        ListDisplayText("item_title", Strings.get("movie_name"), title)
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
            resource = { painterResource },
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp)
                .width(240.dp),
            contentScale = ContentScale.Crop,
            onLoading = {
                CircularProgressIndicator()
            },
            onFailure = { exception ->
                coroutineScope.launch {
                    Logger.i("MACELOG: Kamel EXCEPTION=${exception.message.toString()}")
                }
            },
            animationSpec = tween(durationMillis = 300)
        )

        Divider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = MaterialTheme.colors.onBackground, thickness = 2.dp)
    }

    @Composable
    fun MoviesList() {
        val movies: LazyPagingItems<Movie> = viewModel.moviesAvailableState.collectAsLazyPagingItems()
        if (movies.itemCount > 0) {
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
                                    viewModel.handleIntent(MoviesIntent.apiFailure(it))
                                }
                            }
                        }
                        loadState.append is LoadStateError -> {
                            item {
                                (movies.loadState.append as LoadStateError).error.message?.let {
                                    viewModel.handleIntent(MoviesIntent.apiFailure(it))
                                }
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
                            route = ScreenNames.TravelDestinations.name,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.moviesViewState.value.apiFailure.isEmpty()) {
            MoviesList()
        }
    }
}