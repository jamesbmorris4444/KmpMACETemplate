
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
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

    var rocketLaunchesInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var refreshCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var refreshFailureState: MutableStateFlow<String> = MutableStateFlow("")
    var launchesAvailableState: MutableStateFlow<List<RocketLaunch>> = MutableStateFlow(listOf())

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

    var databaseInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var donorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())


    // End Donate Products Screen state

    fun donorsFromFullNameWithProducts(searchLast: String, dob: String): DonorWithProducts? {
        return repository.donorsFromFullNameWithProducts(searchLast, dob)
    }

    fun insertProductsIntoDatabase(products: List<Product>) {
        repository.insertProductsIntoDatabase(products)
    }

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