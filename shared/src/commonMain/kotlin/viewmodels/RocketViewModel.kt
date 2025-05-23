package viewmodels
import Repository
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import viewstate.RocketViewState

class RocketViewModel : AbstractRocketViewModel()

abstract class AbstractRocketViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    sealed class RocketIntent {
        data object loadLaunches : RocketIntent()
        data object failureDialogDismissed : RocketIntent()
    }

    fun handleIntent(intent: RocketIntent) {
        when (intent) {
            is RocketIntent.loadLaunches -> rocketApiCall()
            is RocketIntent.failureDialogDismissed -> {
                mutableRocketiewState.value = RocketViewState(
                    launchesFailure = ""
                )
            }
        }
    }

    private val mutableRocketiewState = MutableStateFlow(RocketViewState())
    val rocketViewState: StateFlow<RocketViewState> = mutableRocketiewState

    private fun rocketApiCall() {
        val composableScope = viewModelScope.coroutineScope
        composableScope.launch {
            getSpaceXLaunches(composableScope)
        }
    }

    private suspend fun getSpaceXLaunches(composableScope: CoroutineScope) {
        val launchesResponse = repository.getSpaceXLaunches(composableScope)
        mutableRocketiewState.value = RocketViewState(
            launchesAvailable = launchesResponse.first,
            launchesFailure = launchesResponse.second,
            progressBarState = false
        )
    }
}