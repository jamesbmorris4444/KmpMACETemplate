
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.HotelDestinationId
import com.jetbrains.handson.kmm.shared.entity.HotelRegion
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.StandardModalArgs

abstract class ViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    override fun onCleared() {
        super.onCleared()
    }

    val emptyDonor = Donor(0,"", "", "", "", "", "", gender = false)
    val noValue = "NO VALUE"

    var launchesAvailable: MutableStateFlow<List<RocketLaunch>?> = MutableStateFlow(null)
    var launchesFailure: MutableStateFlow<String> = MutableStateFlow("")
    var destinationIdsAvailable: MutableStateFlow<List<HotelDestinationId>?> = MutableStateFlow(null)
    var destinationIdsFailure: MutableStateFlow<String> = MutableStateFlow("")
    var hotelsAvailable: MutableStateFlow<HotelRegion?> = MutableStateFlow(null)
    var regionsFailure: MutableStateFlow<String> = MutableStateFlow("")

    val moviesAvailableState: Flow<PagingData<Movie>> = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { repository.getMoviePagingSource() }
        )
        .flow
        .cachedIn(viewModelScope.coroutineScope)

    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())
    var progressBarState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var donorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())

    fun initializeDatabase() {
        repository.initializeDatabase()
    }

    suspend fun getSpaceXLaunches(composableScope: CoroutineScope): Pair<List<RocketLaunch>, String> {
        return repository.getSpaceXLaunches(composableScope)
    }

    suspend fun getHotelDestinationIds(destinationSearchKey: String, composableScope: CoroutineScope): Pair<List<HotelDestinationId>, String> {
        return repository.getHotelDestinationIds(destinationSearchKey, composableScope)
    }

    suspend fun getHotels(regionSearchKey: String, regionSearchType: String, composableScope: CoroutineScope): Pair<HotelRegion, String> {
        return repository.getHotels(regionSearchKey, regionSearchType, composableScope)
    }

    fun donorsFromFullNameWithProducts(searchLast: String, dob: String): DonorWithProducts? {
        return repository.donorsFromFullNameWithProducts(searchLast, dob)
    }

    fun insertProductsIntoDatabase(products: List<Product>) {
        repository.insertProductsIntoDatabase(products)
    }

    fun handleSearchClick(searchKey:String): List<Donor> {
        return repository.handleSearchClick(searchKey)
    }

    fun handleSearchClickWithProductsCorrectDonor(searchKey: String): List<DonorWithProducts> {
        return repository.handleSearchClickWithProducts(searchKey)
    }

    fun handleSearchClickWithProductsIncorrectDonor(searchKey: String): List<DonorWithProducts> {
        return repository.handleSearchClickWithProducts(searchKey)
    }

    fun updateDonorIdInProduct(correctDonorId: Long, productId: Long) {
        repository.updateDonorIdInProduct(correctDonorId, productId)
    }

    fun donorFromNameAndDateWithProducts(donor: Donor): DonorWithProducts? {
        return repository.donorFromNameAndDateWithProducts(donor)
    }

    fun donorAndProductsList(lastNameSearchKey: String): List<DonorWithProducts> {
        return repository.donorAndProductsList(lastNameSearchKey)
    }

    fun updateDonor(
        firstName: String,
        middleName: String,
        lastName: String,
        dob: String,
        aboRh: String,
        branch: String,
        gender: Boolean,
        id: Long
    ) {
        repository.updateDonor(
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            dob = dob,
            aboRh = aboRh,
            branch = branch,
            gender = gender,
            id = id
        )
    }

    fun insertDonorIntoDatabase(donor: Donor) {
        repository.insertDonorIntoDatabase(donor)
    }
}