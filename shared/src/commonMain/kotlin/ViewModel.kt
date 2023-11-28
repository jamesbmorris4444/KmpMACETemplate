
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel
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

    // Start Rocket Launches Screen state

    private val privateRocketLaunchesInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val rocketLaunchesInvalidState: MutableStateFlow<Boolean>
        get() = privateRocketLaunchesInvalidState

    private val privateRefreshCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val refreshCompletedState: MutableStateFlow<Boolean>
        get() = privateRefreshCompletedState

    private val privateRefreshFailureState: MutableStateFlow<String> = MutableStateFlow("")
    val refreshFailureState: MutableStateFlow<String>
        get() = privateRefreshFailureState

    private val privateLaunchesAvailableState: MutableStateFlow<List<RocketLaunch>> = MutableStateFlow(listOf())
    val launchesAvailableState: MutableStateFlow<List<RocketLaunch>>
        get() = privateLaunchesAvailableState

    fun updateRocketLaunchesInvalidState(value: Boolean) {
        privateRocketLaunchesInvalidState.value = value
    }

    fun updateRefreshCompletedState(value: Boolean) {
        privateRefreshCompletedState.value = value
    }

    fun updateRefreshFailureState(value: String) {
        privateRefreshFailureState.value = value
    }

    fun updateLaunchesAvailableState(launches: List<RocketLaunch>) {
        privateLaunchesAvailableState.value = launches
    }

    // End Rocket Launches Screen state

    // Start Standard Modal Screen state

    private val privateShowStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())
    val showStandardModalState: MutableStateFlow<StandardModalArgs>
        get() = privateShowStandardModalState

    fun changeShowStandardModalState(standardModalArgs: StandardModalArgs) {
        privateShowStandardModalState.value = standardModalArgs
    }

    // End Standard Modal Screen state

    // Start Donate Products Screen state

    private val privateDatabaseInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val databaseInvalidState: MutableStateFlow<Boolean>
        get() = privateDatabaseInvalidState

    private val privateDonorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())
    val donorsAvailableState: MutableStateFlow<List<Donor>>
        get() = privateDonorsAvailableState

    fun updateDatabaseInvalidState(value: Boolean) {
        privateDatabaseInvalidState.value = value
    }

    fun updateDonorsAvailableState(donors: List<Donor>) {
        privateDonorsAvailableState.value = donors
    }

    // End Donate Products Screen state

    // Start Reassociate Donation Screen state

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

    // End Reassociate Donation Screen state

    // Start View Donor List Screen

    fun donorAndProductsList(lastNameSearchKey: String): List<DonorWithProducts> {
        return repository.donorAndProductsList(lastNameSearchKey)
    }

    // End View Donor List Screen
}