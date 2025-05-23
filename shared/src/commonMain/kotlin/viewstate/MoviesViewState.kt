package viewstate

import app.cash.paging.PagingData
import com.jetbrains.handson.kmm.shared.entity.Movie
import kotlinx.coroutines.flow.Flow

data class MoviesViewState (
    var moviesAvailable: Flow<PagingData<Movie>>? = null,
    var apiFailure: String = ""
)