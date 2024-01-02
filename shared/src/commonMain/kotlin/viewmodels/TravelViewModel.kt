package viewmodels
import Repository
import com.jetbrains.handson.kmm.shared.entity.HotelDestinationId
import com.jetbrains.handson.kmm.shared.entity.HotelRegion
import com.mace.corelib.StandardModalArgs
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TravelViewModel : AbstractTravelViewModel()

abstract class AbstractTravelViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    override fun onCleared() {
        super.onCleared()
    }

    var destinationIdsAvailable: MutableStateFlow<List<HotelDestinationId>?> = MutableStateFlow(null)
    var destinationIdsFailure: MutableStateFlow<String> = MutableStateFlow("")
    var hotelsAvailable: MutableStateFlow<HotelRegion?> = MutableStateFlow(null)
    var regionsFailure: MutableStateFlow<String> = MutableStateFlow("")
    var regionsSearchKey: MutableStateFlow<String> = MutableStateFlow("")
    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())

    suspend fun getHotelDestinationIds(destinationSearchKey: String, composableScope: CoroutineScope): Pair<List<HotelDestinationId>, String> {
        return repository.getHotelDestinationIds(destinationSearchKey, composableScope)
    }

    suspend fun getHotels(regionSearchKey: String, regionSearchType: String, composableScope: CoroutineScope): Pair<HotelRegion, String> {
        return repository.getHotels(regionSearchKey, regionSearchType, composableScope)
    }
}