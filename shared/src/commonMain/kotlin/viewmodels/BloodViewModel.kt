package viewmodels
import Repository
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.mace.corelib.StandardModalArgs
import com.rickclephas.kmm.viewmodel.KMMViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BloodViewModel : AbstractBloodViewModel()

abstract class AbstractBloodViewModel : KMMViewModel(), KoinComponent {

    private val repository: Repository by inject()

    override fun onCleared() {
        super.onCleared()
    }

    val emptyDonor = Donor(0,"", "", "", "", "", "", gender = false)
    val noValue = "NO VALUE"

    val showStandardModalState: MutableStateFlow<StandardModalArgs> = MutableStateFlow(StandardModalArgs())
    var donorsAvailableState: MutableStateFlow<List<Donor>> = MutableStateFlow(listOf())
    var productsListState: MutableStateFlow<MutableList<Product>> = MutableStateFlow(mutableListOf())

    fun initializeDatabase() {
        repository.initializeDatabase()
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