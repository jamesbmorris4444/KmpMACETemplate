import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ui.StandardModalArgs

expect abstract class ViewModel() : KMMViewModel {
    override fun onCleared()
    val emptyDonor: Donor
    internal val privateRefreshCompletedState: MutableStateFlow<Boolean>
    val refreshCompletedState: MutableStateFlow<Boolean>
    internal val privateDatabaseInvalidState: MutableStateFlow<Boolean>
    val databaseInvalidState: MutableStateFlow<Boolean>
    internal val privateRefreshFailureState: MutableStateFlow<String>
    val refreshFailureState: MutableStateFlow<String>
    internal val privateLaunchesAvailableState: MutableStateFlow<List<RocketLaunch>>
    val launchesAvailableState: MutableStateFlow<List<RocketLaunch>>
    internal val privateDonorsAvailableState: MutableStateFlow<List<Donor>>
    val donorsAvailableState: MutableStateFlow<List<Donor>>
    internal val privateShowStandardModalState: MutableStateFlow<StandardModalArgs>
    val showStandardModalState: MutableStateFlow<StandardModalArgs>
    fun updateRefreshCompletedState(value: Boolean)
    fun updateDatabaseInvalidState(value: Boolean)
    fun updateRefreshFailureState(value: String)
    fun updateLaunchesAvailableState(launches: List<RocketLaunch>)
    fun updateDonorsAvailableState(donors: List<Donor>)
    fun changeShowStandardModalState(standardModalArgs: StandardModalArgs)

    // Start Reassociate Donation Screen state

    internal val privateCorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
    val correctDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
    internal val privateIncorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
    val incorrectDonorsWithProductsState: MutableStateFlow<List<DonorWithProducts>>
    fun changeCorrectDonorsWithProductsState(list: List<DonorWithProducts>)
    fun changeIncorrectDonorsWithProductsState(list: List<DonorWithProducts>)
    internal val privateCorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts>
    val correctDonorWithProductsStatee: MutableStateFlow<DonorWithProducts>
    internal val privateIncorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts>
    val incorrectDonorWithProductsState: MutableStateFlow<DonorWithProducts>
    fun changeCorrectDonorWithProductsState(donor: Donor)
    fun changeIncorrectDonorWithProductsState(donor: Donor)
    internal val privateSingleSelectedProductListState: MutableStateFlow<List<Product>>
    val singleSelectedProductListState: MutableStateFlow<List<Product>>
    fun changeSingleSelectedProductListState(list: List<Product>)
    internal val privateIncorrectDonorSelectedState: MutableStateFlow<Boolean>
    val incorrectDonorSelectedState: MutableStateFlow<Boolean>
    internal val privateIsProductSelectedState: MutableStateFlow<Boolean>
    val isProductSelectedState: MutableStateFlow<Boolean>
    internal val privateIsReassociateCompletedState: MutableStateFlow<Boolean>
    val isReassociateCompletedState: MutableStateFlow<Boolean>
    fun changeIncorrectDonorSelectedState(state: Boolean)
    fun changeIsProductSelectedState(state: Boolean)
    fun changeIsReassociateCompletedState(state: Boolean)
    fun resetReassociateCompletedScreen()

    // End Reassociate Donation Screen state

    // Start Create Products Screen state

    internal val privateDinTextState: MutableStateFlow<String>
    val dinTextState: MutableStateFlow<String>
    internal val privateProductCodeTextState: MutableStateFlow<String>
    val productCodeTextState: MutableStateFlow<String>
    internal val privateExpirationTextState: MutableStateFlow<String>
    val expirationTextState: MutableStateFlow<String>
    fun changeDinTextState(text: String)
    fun changeProductCodeTextState(text: String)
    fun changeExpirationTextState(text: String)
    internal val privateClearButtonVisibleState: MutableStateFlow<Boolean>
    val clearButtonVisibleState: MutableStateFlow<Boolean>
    internal val privateConfirmButtonVisibleState: MutableStateFlow<Boolean>
    val confirmButtonVisibleState: MutableStateFlow<Boolean>
    internal val privateConfirmNeededState: MutableStateFlow<Boolean>
    val confirmNeededState: MutableStateFlow<Boolean>
    fun changeClearButtonVisibleState(state: Boolean)
    fun changeConfirmButtonVisibleState(state: Boolean)
    fun changeConfirmNeededState(state: Boolean)
    internal val privateProductsListState: MutableStateFlow<List<Product>>
    val productsListState: MutableStateFlow<List<Product>>
    fun changeProductsListState(list: List<Product>)
    internal val privateDisplayedProductListState: MutableStateFlow<List<Product>>
    val displayedProductListState: MutableStateFlow<List<Product>>
    fun changeDisplayedProductListState(list: List<Product>)
}