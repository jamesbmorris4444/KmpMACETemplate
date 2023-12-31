package viewmodels
import Repository
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.mace.corelib.StandardModalArgs
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieViewModel : AbstractMovieViewModel()

abstract class AbstractMovieViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    override fun onCleared() {
        super.onCleared()
    }

    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())

    val moviesAvailableState: Flow<PagingData<Movie>> = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { repository.getMoviePagingSource() }
        )
        .flow
        .cachedIn(viewModelScope.coroutineScope)
}