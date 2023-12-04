
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.CoroutineScope
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

    var rocketLaunchesInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var refreshCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var refreshFailureState: MutableStateFlow<String> = MutableStateFlow("")
    var launchesAvailableState: MutableStateFlow<List<RocketLaunch>> = MutableStateFlow(listOf())

    var moviesInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var moviesCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var moviesFailureState: MutableStateFlow<String> = MutableStateFlow("")
    var moviesAvailableState: MutableStateFlow<List<Movie>> = MutableStateFlow(listOf())

    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())
    var databaseInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var donorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())

    fun initializeDatabase() {
        repository.initializeDatabase()
    }

    suspend fun getMovies(composableScope: CoroutineScope): Pair<List<Movie>, String> {
        return repository.getMovies(composableScope)
    }

    suspend fun getSpaceXLaunches(composableScope: CoroutineScope): Pair<List<RocketLaunch>, String> {
        return repository.getSpaceXLaunches(composableScope)
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

    fun updateProductRemovedForReassociation(newValue: Boolean, productId: Long) {
        repository.updateProductRemovedForReassociation(newValue, productId)
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