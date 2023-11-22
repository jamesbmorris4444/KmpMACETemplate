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

    val emptyDonor = Donor(0,"", "", "", "", "", "", false, false)

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

    private val privateRefreshEditTextState: MutableStateFlow<String> = MutableStateFlow("")
    val refreshEditTextState: MutableStateFlow<String>
        get() = privateRefreshEditTextState

    fun updateDatabaseInvalidState(value: Boolean) {
        privateDatabaseInvalidState.value = value
    }

    fun updateDonorsAvailableState(donors: List<Donor>) {
        privateDonorsAvailableState.value = donors
    }

    fun refreshEditTextState(text: String) {
        privateRefreshEditTextState.value = text
    }

    // End Donate Products Screen state

    // Start Reassociate Donation Screen state

    fun handleSearchClickWithProductsCorrectDonor(searchKey: String) {
        privateCorrectDonorsWithProductsState.value = repository.handleSearchClickWithProducts(searchKey)
    }

    fun handleSearchClickWithProductsIncorrectDonor(searchKey: String) {
        privateIncorrectDonorsWithProductsState.value = repository.handleSearchClickWithProducts(searchKey)
    }

    private val privateCorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>> = MutableStateFlow(listOf())
    val correctDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
        get() = privateCorrectDonorsWithProductsState

    private val privateIncorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>> = MutableStateFlow(listOf())
    val incorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
        get() = privateIncorrectDonorsWithProductsState

    fun changeCorrectDonorsWithProductsState(list: List<DonorWithProducts>) {
        privateCorrectDonorsWithProductsState.value = list
    }

    fun changeIncorrectDonorsWithProductsState(list: List<DonorWithProducts>) {
        privateIncorrectDonorsWithProductsState.value = list
    }

    private val privateCorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts> = MutableStateFlow(DonorWithProducts(emptyDonor))
    val correctDonorWithProductsState: MutableStateFlow<DonorWithProducts>
        get() = privateCorrectDonorWithProductsState

    private val privateIncorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts> = MutableStateFlow(DonorWithProducts(emptyDonor))
    val incorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts>
        get() = privateIncorrectDonorWithProductsState

    fun changeCorrectDonorWithProductsState(donor: Donor) {
        privateCorrectDonorWithProductsState.value = repository.donorFromNameAndDateWithProducts(donor) ?: DonorWithProducts(emptyDonor, listOf())
    }

    fun changeIncorrectDonorWithProductsState(donor: Donor) {
        privateIncorrectDonorWithProductsState.value = repository.donorFromNameAndDateWithProducts(donor) ?: DonorWithProducts(emptyDonor, listOf())
    }

    private val privateSingleSelectedProductListState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    val singleSelectedProductListState: MutableStateFlow<List<Product>>
        get() = privateSingleSelectedProductListState

    fun changeSingleSelectedProductListState(list: List<Product>) {
        privateSingleSelectedProductListState.value = list
    }

    private val privateIncorrectDonorSelectedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val incorrectDonorSelectedState: MutableStateFlow<Boolean>
        get() = privateIncorrectDonorSelectedState

    private val privateIsProductSelectedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProductSelectedState: MutableStateFlow<Boolean>
        get() = privateIsProductSelectedState

    private val privateIsReassociateCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isReassociateCompletedState: MutableStateFlow<Boolean>
        get() = privateIsReassociateCompletedState

    fun changeIncorrectDonorSelectedState(state: Boolean) {
        privateIncorrectDonorSelectedState.value = state
    }

    fun changeIsProductSelectedState(state: Boolean) {
        privateIsProductSelectedState.value = state
    }

    fun changeIsReassociateCompletedState(state: Boolean) {
        privateIsReassociateCompletedState.value = state
    }

    fun resetReassociateCompletedScreen() {
        privateCorrectDonorsWithProductsState.value = listOf()
        privateIncorrectDonorsWithProductsState.value = listOf()
        privateCorrectDonorWithProductsState.value = DonorWithProducts(emptyDonor)
        privateIncorrectDonorWithProductsState.value = DonorWithProducts(emptyDonor)
        privateSingleSelectedProductListState.value = listOf()
        privateIncorrectDonorSelectedState.value = false
        privateIsProductSelectedState.value = false
        privateIsReassociateCompletedState.value = false
    }

    // End Reassociate Donation Screen state

    // Start Create Products Screen state

    private val privateDinTextState: MutableStateFlow<String> = MutableStateFlow("")
    val dinTextState: MutableStateFlow<String>
        get() = privateDinTextState

    private val privateProductCodeTextState: MutableStateFlow<String> = MutableStateFlow("")
    val productCodeTextState: MutableStateFlow<String>
        get() = privateProductCodeTextState

    private val privateExpirationTextState: MutableStateFlow<String> = MutableStateFlow("")
    val expirationTextState: MutableStateFlow<String>
        get() = privateExpirationTextState

    fun changeDinTextState(text: String) {
        privateDinTextState.value = text
    }

    fun changeProductCodeTextState(text: String) {
        privateProductCodeTextState.value = text
    }

    fun changeExpirationTextState(text: String) {
        privateExpirationTextState.value = text
    }

    private val privateClearButtonVisibleState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val clearButtonVisibleState: MutableStateFlow<Boolean>
        get() = privateClearButtonVisibleState

    private val privateConfirmButtonVisibleState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val confirmButtonVisibleState: MutableStateFlow<Boolean>
        get() = privateConfirmButtonVisibleState

    private val privateCompleteButtonVisibleState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val completeButtonVisibleState: MutableStateFlow<Boolean>
        get() = privateCompleteButtonVisibleState

    private val privateScreenIsReadOnlyState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val screenIsReadOnlyState: MutableStateFlow<Boolean>
        get() = privateScreenIsReadOnlyState

    fun changeClearButtonVisibleState(state: Boolean) {
        privateClearButtonVisibleState.value = state
    }

    fun changeConfirmButtonVisibleState(state: Boolean) {
        privateConfirmButtonVisibleState.value = state
    }

    fun changeCompleteButtonVisibleState(state: Boolean) {
        privateCompleteButtonVisibleState.value = state
    }

    fun changeScreenIsReadOnlyState(state: Boolean) {
        privateScreenIsReadOnlyState.value = state
    }

    private val privateProductsListState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    val productsListState: MutableStateFlow<List<Product>>
        get() = privateProductsListState

    fun changeProductsListState(list: List<Product>) {
        privateProductsListState.value = list
    }

    // End Create Products Screen state
}