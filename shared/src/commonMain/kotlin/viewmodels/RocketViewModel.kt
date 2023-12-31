package viewmodels
import Repository
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.mace.corelib.StandardModalArgs
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RocketViewModel : AbstractRocketViewModel()

abstract class AbstractRocketViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    override fun onCleared() {
        super.onCleared()
    }

    var launchesAvailable: MutableStateFlow<List<RocketLaunch>?> = MutableStateFlow(null)
    var launchesFailure: MutableStateFlow<String> = MutableStateFlow("")
    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())
    var progressBarState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun getSpaceXLaunches(composableScope: CoroutineScope): Pair<List<RocketLaunch>, String> {
        return repository.getSpaceXLaunches(composableScope)
    }
}