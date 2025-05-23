package viewmodels
import Repository
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import viewstate.MoviesViewState

class MovieViewModel : AbstractMovieViewModel()

abstract class AbstractMovieViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()


    val moviesAvailableState: Flow<PagingData<Movie>> = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { repository.getMoviePagingSource() }
        )
        .flow
        .cachedIn(viewModelScope.coroutineScope)

    sealed class MoviesIntent {
        data object loadMovies : MoviesIntent()
        data class apiFailure(val message: String) : MoviesIntent()
    }

    fun handleIntent(intent: MoviesIntent) {
        when (intent) {
            is MoviesIntent.loadMovies -> moviesApiCall()
            is MoviesIntent.apiFailure -> {
                mutableMoviesViewState.value = MoviesViewState(
                    apiFailure = intent.message
                )
            }
        }
    }

    private val mutableMoviesViewState = MutableStateFlow(MoviesViewState())
    val moviesViewState: StateFlow<MoviesViewState> = mutableMoviesViewState

    private fun moviesApiCall() {
        val composableScope = viewModelScope.coroutineScope
        composableScope.launch {
            getMovies()
        }
    }

    private fun getMovies() {
        mutableMoviesViewState.value = MoviesViewState(
            moviesAvailable = moviesAvailableState
        )
    }
}