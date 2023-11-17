import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual abstract class ViewModel actual constructor() : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    actual override fun onCleared() {}

    actual val emptyDonor = Donor(0,"", "", "", "", "", "", false, false)

    internal actual val privateRefreshCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    actual val refreshCompletedState: MutableStateFlow<Boolean>
        get() = privateRefreshCompletedState

    internal actual val privateDatabaseInvalidState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    actual val databaseInvalidState: MutableStateFlow<Boolean>
        get() = privateDatabaseInvalidState

    internal actual val privateRefreshFailureState: MutableStateFlow<String> = MutableStateFlow("")
    actual val refreshFailureState: MutableStateFlow<String>
        get() = privateRefreshFailureState

    internal actual val privateLaunchesAvailableState: MutableStateFlow<List<RocketLaunch>> = MutableStateFlow(listOf())
    actual val launchesAvailableState: MutableStateFlow<List<RocketLaunch>>
        get() = privateLaunchesAvailableState

    internal actual val privateDonorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())
    actual val donorsAvailableState: MutableStateFlow<List<Donor>>
        get() = privateDonorsAvailableState


    internal actual val privateShowStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(
        StandardModalArgs()
    )
    actual val showStandardModalState: MutableStateFlow<StandardModalArgs>
        get() = privateShowStandardModalState

    actual fun updateRefreshCompletedState(value: Boolean) {
        privateRefreshCompletedState.value = value
    }

    actual fun updateDatabaseInvalidState(value: Boolean) {
        privateDatabaseInvalidState.value = value
    }

    actual fun updateRefreshFailureState(value: String) {
        privateRefreshFailureState.value = value
    }

    actual fun updateLaunchesAvailableState(launches: List<RocketLaunch>) {
        privateLaunchesAvailableState.value = launches
    }

    actual fun updateDonorsAvailableState(donors: List<Donor>) {
        privateDonorsAvailableState.value = donors
    }

    actual fun changeShowStandardModalState(standardModalArgs: StandardModalArgs) {
        privateShowStandardModalState.value = standardModalArgs
    }

    // Start Reassociate Donation Screen state

    internal actual val privateCorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>> = MutableStateFlow(listOf())
    actual val correctDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
        get() = privateCorrectDonorsWithProductsState

    internal actual val privateIncorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>> = MutableStateFlow(listOf())
    actual val incorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
        get() = privateIncorrectDonorsWithProductsState

    actual fun changeCorrectDonorsWithProductsState(list: List<DonorWithProducts>) {
        privateCorrectDonorsWithProductsState.value = list
    }

    actual fun changeIncorrectDonorsWithProductsState(list: List<DonorWithProducts>) {
        privateIncorrectDonorsWithProductsState.value = list
    }

    internal actual val privateCorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts> = MutableStateFlow(DonorWithProducts(emptyDonor))
    actual val correctDonorWithProductsStatee: MutableStateFlow<DonorWithProducts>
        get() = privateCorrectDonorWithProductsState

    internal actual val privateIncorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts> = MutableStateFlow(DonorWithProducts(emptyDonor))
    actual val incorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts>
        get() = privateIncorrectDonorWithProductsState

    actual fun changeCorrectDonorWithProductsState(donor: Donor) {
        privateCorrectDonorWithProductsState.value = repository.donorFromNameAndDateWithProducts(donor) ?: DonorWithProducts(emptyDonor, listOf())
    }

    actual fun changeIncorrectDonorWithProductsState(donor: Donor) {
        privateIncorrectDonorWithProductsState.value = repository.donorFromNameAndDateWithProducts(donor) ?: DonorWithProducts(emptyDonor, listOf())
    }

    internal actual val privateSingleSelectedProductListState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    actual val singleSelectedProductListState: MutableStateFlow<List<Product>>
        get() = privateSingleSelectedProductListState

    actual fun changeSingleSelectedProductListState(list: List<Product>) {
        privateSingleSelectedProductListState.value = list
    }

    internal actual val privateIncorrectDonorSelectedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual val incorrectDonorSelectedState: MutableStateFlow<Boolean>
        get() = privateIncorrectDonorSelectedState

    internal actual val privateIsProductSelectedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual val isProductSelectedState: MutableStateFlow<Boolean>
        get() = privateIsProductSelectedState

    internal actual val privateIsReassociateCompletedState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual val isReassociateCompletedState: MutableStateFlow<Boolean>
        get() = privateIsReassociateCompletedState

    actual fun changeIncorrectDonorSelectedState(state: Boolean) {
        privateIncorrectDonorSelectedState.value = state
    }

    actual fun changeIsProductSelectedState(state: Boolean) {
        privateIsProductSelectedState.value = state
    }

    actual fun changeIsReassociateCompletedState(state: Boolean) {
        privateIsReassociateCompletedState.value = state
    }

    actual fun resetReassociateCompletedScreen() {
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

    internal actual val privateDinTextState: MutableStateFlow<String> = MutableStateFlow("")
    actual val dinTextState: MutableStateFlow<String>
        get() = privateDinTextState

    internal actual val privateProductCodeTextState: MutableStateFlow<String> = MutableStateFlow("")
    actual val productCodeTextState: MutableStateFlow<String>
        get() = privateProductCodeTextState

    internal actual val privateExpirationTextState: MutableStateFlow<String> = MutableStateFlow("")
    actual val expirationTextState: MutableStateFlow<String>
        get() = privateExpirationTextState

    actual fun changeDinTextState(text: String) {
        privateDinTextState.value = text
    }

    actual fun changeProductCodeTextState(text: String) {
        privateProductCodeTextState.value = text
    }

    actual fun changeExpirationTextState(text: String) {
        privateExpirationTextState.value = text
    }

    internal actual val privateClearButtonVisibleState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    actual val clearButtonVisibleState: MutableStateFlow<Boolean>
        get() = privateClearButtonVisibleState

    internal actual val privateConfirmButtonVisibleState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    actual val confirmButtonVisibleState: MutableStateFlow<Boolean>
        get() = privateConfirmButtonVisibleState

    internal actual val privateConfirmNeededState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    actual val confirmNeededState: MutableStateFlow<Boolean>
        get() = privateConfirmNeededState

    actual fun changeClearButtonVisibleState(state: Boolean) {
        privateClearButtonVisibleState.value = state
    }

    actual fun changeConfirmButtonVisibleState(state: Boolean) {
        privateConfirmButtonVisibleState.value = state
    }

    actual fun changeConfirmNeededState(state: Boolean) {
        privateConfirmNeededState.value = state
    }

    internal actual val privateProductsListState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    actual val productsListState: MutableStateFlow<List<Product>>
        get() = privateProductsListState

    actual fun changeProductsListState(list: List<Product>) {
        privateProductsListState.value = list
    }

    internal actual val privateDisplayedProductListState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    actual val displayedProductListState: MutableStateFlow<List<Product>>
        get() = privateDisplayedProductListState

    actual fun changeDisplayedProductListState(list: List<Product>) {
        privateDisplayedProductListState.value = list
    }

    // End Create Products Screen state

}