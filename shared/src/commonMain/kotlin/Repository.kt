
import androidx.paging.PagingSource
import app.cash.paging.PagingState
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.jetbrains.handson.kmm.shared.entity.HotelDestinationId
import com.jetbrains.handson.kmm.shared.entity.HotelRegion
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.jetbrains.handson.kmm.shared.entity.MoviesWithPageNumber
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface Repository {
    var screenWidth: Int
    var screenHeight: Int
    suspend fun getSpaceXLaunches(composableScope: CoroutineScope): Pair<List<RocketLaunch>, String>
    fun getMoviePagingSource(): MoviePagingSource
    suspend fun getHotels(destId: String, destType: String, composableScope: CoroutineScope): Pair<HotelRegion, String>
    suspend fun getHotelDestinationIds(destinationSearchKey: String, composableScope: CoroutineScope): Pair<List<HotelDestinationId>, String>
    fun initializeDatabase()
    fun insertDonorIntoDatabase(donor: Donor)
    fun insertProductsIntoDatabase(products: List<Product>)
    fun donorAndProductsList(lastNameSearchKey: String): List<DonorWithProducts>
    fun donorsFromFullNameWithProducts(searchLast: String, dob: String): DonorWithProducts?
    fun handleSearchClick(searchKey: String) : List<Donor>
    fun handleSearchClickWithProducts(searchKey: String) : List<DonorWithProducts>
    fun donorFromNameAndDateWithProducts(donor: Donor): DonorWithProducts?
    fun updateDonorIdInProduct(newValue: Long, id: Long)
    fun updateDonor(firstName: String, middleName: String, lastName: String, dob: String, aboRh: String, branch: String, gender: Boolean, id: Long)
}

class RepositoryImpl : Repository, KoinComponent {

    private val sdk: SpaceXSDK by inject()
    private val databaseDriverFactory: DatabaseDriverFactory by inject()

    override var screenWidth = 0
    override var screenHeight = 0

    override suspend fun getSpaceXLaunches(composableScope: CoroutineScope): Pair<List<RocketLaunch>, String> {
        var result: List<RocketLaunch> = listOf()
        var message = ""
        try {
            result = sdk.getLaunches()
            Logger.i("MACELOG: SpaceX launches success: ${result.size}")
        } catch (e: Exception) {
            message = e.message ?: "NULL message"
            Logger.e("MACELOG: SpaceX launches failure: ${e.message}")
        }
        return Pair(result, message)
    }

    override suspend fun getHotelDestinationIds(destinationSearchKey: String, composableScope: CoroutineScope): Pair<List<HotelDestinationId>, String> {
        val baseUrl = "https://apidojo-booking-v1.p.rapidapi.com/locations/auto-complete"
        val headerKeyKey = "X-RapidAPI-Key"
        val headerKeyValue = "f674a66b81msh663e5810b0cdd7ep162635jsnb30c4b837dd9"
        val headerHostKey = "X-RapidAPI-Host"
        val headerHostValue = "apidojo-booking-v1.p.rapidapi.com"
        val textRequestParamKey = "text"
        val textRequestParamValue = destinationSearchKey
        val languageRequestParamKey = "languagecode"
        val languageRequestParamValue = "en"
        var result: List<HotelDestinationId> = listOf()
        var message = ""

        try {
            val httpClient = HttpClient()
            val jsonSerializer = Json {
                ignoreUnknownKeys = true
                isLenient = false
            }
            val response: HttpResponse = httpClient.request(
                baseUrl
            ) {
                this.method = HttpMethod.Get
                this.header(headerKeyKey, headerKeyValue)
                this.header(headerHostKey, headerHostValue)
                this.parameter(textRequestParamKey, textRequestParamValue)
                this.parameter(languageRequestParamKey, languageRequestParamValue)
            }
            val responseBody = response.bodyAsText()
            result = jsonSerializer.decodeFromString(responseBody)
            Logger.i("MACELOG: getHotelDestinationIds success: ${result.size}")
        } catch (e: Exception) {
            message = e.message ?: "NULL message"
            Logger.e("MACELOG: getHotelDestinationIds failure: ${e.message}")
        }
        return Pair(result, message)
    }

    override suspend fun getHotels(destId: String, destType: String, composableScope: CoroutineScope): Pair<HotelRegion, String> {
        val baseUrl = "https://apidojo-booking-v1.p.rapidapi.com/properties/list"
        val headerKeyKey = "X-RapidAPI-Key"
        val headerKeyValue = "f674a66b81msh663e5810b0cdd7ep162635jsnb30c4b837dd9"
        val headerHostKey = "X-RapidAPI-Host"
        val headerHostValue = "apidojo-booking-v1.p.rapidapi.com"
        val offsetRequestParamKey = "offset"
        val offsetRequestParamValue = "0"
        val arrivalDateRequestParamKey = "arrival_date"
        val arrivalDateRequestParamValue = "2024-01-15"
        val departureDateRequestParamKey = "departure_date"
        val departureDateRequestParamValue = "2024-01-23"
        val guestQtyRequestParamKey = "guest_qty"
        val guestQtyRequestParamValue = "2"
        val destIdsRequestParamKey = "dest_ids"
        val roomQtyRequestParamKey = "room_qty"
        val roomQtyRequestParamValue = "1"
        val searchTypeRequestParamKey = "search_type"
        val languageRequestParamKey = "languagecode"
        val languageRequestParamValue = "en"
        lateinit var result: HotelRegion
        var message = ""

        try {
            val httpClient = HttpClient()
            val jsonSerializer = Json {
                ignoreUnknownKeys = true
                isLenient = false
            }
            val response: HttpResponse = httpClient.request(
                baseUrl
            ) {
                this.method = HttpMethod.Get
                this.header(headerKeyKey, headerKeyValue)
                this.header(headerHostKey, headerHostValue)
                this.parameter(offsetRequestParamKey, offsetRequestParamValue)
                this.parameter(arrivalDateRequestParamKey, arrivalDateRequestParamValue)
                this.parameter(departureDateRequestParamKey, departureDateRequestParamValue)
                this.parameter(guestQtyRequestParamKey, guestQtyRequestParamValue)
                this.parameter(destIdsRequestParamKey, destId)
                this.parameter(roomQtyRequestParamKey, roomQtyRequestParamValue)
                this.parameter(searchTypeRequestParamKey, destType)
                this.parameter(languageRequestParamKey, languageRequestParamValue)
            }
            val responseBody = response.bodyAsText()
            result = jsonSerializer.decodeFromString(responseBody)
            Logger.i("MACELOG: getHotels success: ${result.hotelResult.size}")
        } catch (e: Exception) {
            result = HotelRegion()
            message = e.message ?: "NULL message"
            Logger.e("MACELOG: getHotels failure: ${e.message}")
        }
        return Pair(result, message)
    }

    override fun getMoviePagingSource(): MoviePagingSource {
        return MoviePagingSource()
    }

    override fun initializeDatabase() {
        val list = Database(databaseDriverFactory).getAllDonors()
        Logger.i("MACELOG: number of donors=${list.size}")
        if (list.isEmpty()) {
            Logger.i("MACELOG: DB initialize")
            Database(databaseDriverFactory).clearDatabase()
            Database(databaseDriverFactory).createDonor(createListOfDonors())
        }
    }

    private fun createListOfDonors() : List<Donor> {
        val donorList: MutableList<Donor> = mutableListOf()
        for (index in 0 until 20) {
            val lastName: String  = when (index) {
                0 -> { "Morris01" }
                1 -> { "Smith01" }
                2 -> { "Taylor01" }
                3 -> { "Lewis01" }
                4 -> { "Snowdon01" }
                5 -> { "Miller01" }
                6 -> { "Jones01" }
                7 -> { "Johnson01" }
                8 -> { "Early01" }
                9 -> { "Wynn01" }
                10 -> { "Morris02" }
                11 -> { "Smith02" }
                12 -> { "Taylor02" }
                13 -> { "Lewis02" }
                14 -> { "Snowdon02" }
                15 -> { "Miller02" }
                16 -> { "Jones02" }
                17 -> { "Johnson02" }
                18 -> { "Early02" }
                19 -> { "Wynn02" }
                else -> { "" }
            }
            val firstName: String  = when (index) {
                0 -> { "FirstMorris01" }
                1 -> { "FirstSmith01" }
                2 -> { "FirstTaylor01" }
                3 -> { "FirstLewis01" }
                4 -> { "FirstSnowdon01" }
                5 -> { "FirstMiller01" }
                6 -> { "FirstJones01" }
                7 -> { "FirstJohnson01" }
                8 -> { "FirstEarly01" }
                9 -> { "FirstWynn01" }
                10 -> { "FirstMorris02" }
                11 -> { "FirstSmith02" }
                12 -> { "FirstTaylor02" }
                13 -> { "FirstLewis02" }
                14 -> { "FirstSnowdon02" }
                15 -> { "FirstMiller02" }
                16 -> { "FirstJones02" }
                17 -> { "FirstJohnson02" }
                18 -> { "FirstEarly02" }
                19 -> { "FirstWynn02" }
                else -> { "" }
            }
            val middleName: String  = when (index) {
                0 -> { "MiddleMorris01" }
                1 -> { "MiddleSmith01" }
                2 -> { "MiddleTaylor01" }
                3 -> { "MiddleLewis01" }
                4 -> { "MiddleSnowdon01" }
                5 -> { "MiddleMiller01" }
                6 -> { "MiddleJones01" }
                7 -> { "MiddleJohnson01" }
                8 -> { "MiddleEarly01" }
                9 -> { "MiddleWynn01" }
                10 -> { "MiddleMorris02" }
                11 -> { "MiddleSmith02" }
                12 -> { "MiddleTaylor02" }
                13 -> { "MiddleLewis02" }
                14 -> { "MiddleSnowdon02" }
                15 -> { "MiddleMiller02" }
                16 -> { "MiddleJones02" }
                17 -> { "MiddleJohnson02" }
                18 -> { "MiddleEarly02" }
                19 -> { "MiddleWynn02" }
                else -> { "" }
            }
            val aboRh: String  = when (index) {
                0 -> { "O-Positive" }
                1 -> { "O-Negative" }
                2 -> { "A-Positive" }
                3 -> { "A-Negative" }
                4 -> { "B-Positive" }
                5 -> { "B-Negative" }
                6 -> { "AB-Positive" }
                7 -> { "AB-Negative" }
                8 -> { "O-Positive" }
                9 -> { "O-Negative" }
                10 -> { "A-Positive" }
                11 -> { "A-Negative" }
                12 -> { "B-Positive" }
                13 -> { "B-Negative" }
                14 -> { "AB-Positive" }
                15 -> { "AB-Negative" }
                16 -> { "O-Positive" }
                17 -> { "O-Negative" }
                18 -> { "A-Positive" }
                19 -> { "A-Negative" }
                else -> { "" }
            }
            val dob: String  = when (index) {
                0 -> { "01 Jan 1995" }
                1 -> { "02 Jan 1995" }
                2 -> { "03 Jan 1995" }
                3 -> { "04 Jan 1995" }
                4 -> { "05 Jan 1995" }
                5 -> { "06 Jan 1995" }
                6 -> { "07 Jan 1995" }
                7 -> { "08 Jan 1995" }
                8 -> { "09 Jan 1995" }
                9 -> { "10 Jan 1995" }
                10 -> { "11 Jan 1995" }
                11 -> { "12 Jan 1995" }
                12 -> { "13 Jan 1995" }
                13 -> { "14 Jan 1995" }
                14 -> { "15 Jan 1995" }
                15 -> { "16 Jan 1995" }
                16 -> { "17 Jan 1995" }
                17 -> { "18 Jan 1995" }
                18 -> { "19 Jan 1995" }
                19 -> { "20 Jan 1995" }
                else -> { "" }
            }
            val branch: String  = when (index) {
                0 -> { "The Army" }
                1 -> { "The Army" }
                2 -> { "The Army" }
                3 -> { "The Army" }
                4 -> { "The Army" }
                5 -> { "The Army" }
                6 -> { "The Marines" }
                7 -> { "The Marines" }
                8 -> { "The Marines" }
                9 -> { "The Marines" }
                10 -> { "The Navy" }
                11 -> { "The Navy" }
                12 -> { "The Navy" }
                13 -> { "The Navy" }
                14 -> { "The Navy" }
                15 -> { "The Air Force" }
                16 -> { "The Air Force" }
                17 -> { "The Air Force" }
                18 -> { "The JCS" }
                19 -> { "The JCS" }
                else -> { "" }
            }
            donorList.add(Donor(id = 1L, lastName = lastName, middleName = middleName, firstName = firstName, aboRh = aboRh, dob = dob, branch = branch, gender = true))
        }
        return donorList
    }

    /**
     * The code below here does CRUD on the database
     * Methods:
     *   insertDonorIntoDatabase
     *   insertProductsIntoDatabase
     *   insertReassociatedProductsIntoDatabase
     *   donorAndProductsList
     *   donorFromNameAndDateWithProducts
     *   donorAndProductsList
     *   databaseDonorCount
     *   handleSearchClick
     *   handleSearchClickWithProducts
     *   donorsFromFullNameWithProducts
     */

    override fun insertDonorIntoDatabase(donor: Donor) {
        Database(databaseDriverFactory).insertDonor(donor)
    }

    override fun insertProductsIntoDatabase(products: List<Product>) {
        Database(databaseDriverFactory).insertProductsIntoDatabase(products)
    }

    override fun donorAndProductsList(lastNameSearchKey: String): List<DonorWithProducts> {
        val donors = Database(databaseDriverFactory).getDonors(lastNameSearchKey)
        return donors.map {
            DonorWithProducts(donor = it, products = Database(databaseDriverFactory).selectProductsList(it.id))
        }
    }

    override fun donorFromNameAndDateWithProducts(donor: Donor): DonorWithProducts? {
        return Database(databaseDriverFactory).donorFromNameAndDateWithProducts(donor.lastName, donor.dob)
    }

    override fun handleSearchClick(searchKey: String) : List<Donor> {
        return Database(databaseDriverFactory).getDonors(searchKey)

    }

    override fun handleSearchClickWithProducts(searchKey: String) : List<DonorWithProducts> {
        val donorsResponseList =  Database(databaseDriverFactory).getDonors(searchKey)
        val products: List<Product> = Database(databaseDriverFactory).getAllProducts()
        val donorsWithProducts = donorsResponseList.map { donor ->
            DonorWithProducts(donor = donor, products = products.filter { it.donorId == donor.id })
        }
        return donorsWithProducts
    }

    override fun donorsFromFullNameWithProducts(searchLast: String, dob: String): DonorWithProducts? {
        return Database(databaseDriverFactory).donorFromNameAndDateWithProducts(searchLast, dob)
    }

    override fun updateDonorIdInProduct(newValue: Long, id: Long) {
        Database(databaseDriverFactory).updateDonorIdInProduct(newValue, id)
    }

    override fun updateDonor(firstName: String, middleName: String, lastName: String, dob: String, aboRh: String, branch: String, gender: Boolean, id: Long) {
        Database(databaseDriverFactory).updateDonor(firstName, middleName, lastName, dob, aboRh, branch, gender, id)
    }
}

class MoviePagingSource : PagingSource<Int, Movie>() {
    private val startingKey = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val key = params.key ?: startingKey
        var loadResult = LoadResult.Page(
            data = getMovies(key),
            prevKey = when (key) {
                startingKey -> null
                else -> ensureValidKey(key = key - 1)
            },
            nextKey = key + 1
        )
        return loadResult
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return startingKey
    }

    private fun ensureValidKey(key: Int) = kotlin.math.max(startingKey, key)

    private val baseUrl = "https://api.themoviedb.org/3/discover/movie"
    private val pageRequestParam = "page"
    private val apiKeyRequestParam = "api_key"
    private val apiKey = "17c5889b399d3c051099e4098ad83493"
    private val languageRequestParam = "language"
    private val language = "en"

    private suspend fun getMovies(key: Int): List<Movie> {
        try {
            val httpClient = HttpClient()
            val jsonSerializer = Json {
                ignoreUnknownKeys = true
                isLenient = false
            }
            val response: HttpResponse = httpClient.request(
                baseUrl
            ) {
                this.method = HttpMethod.Get
                this.parameter(pageRequestParam, key.toString())
                this.parameter(apiKeyRequestParam, apiKey)
                this.parameter(languageRequestParam, language)
            }
            val responseBody = response.bodyAsText()
            val moviesWithPageNumber: MoviesWithPageNumber =  jsonSerializer.decodeFromString(responseBody)
            return moviesWithPageNumber.results.filter { it.genreIds.any { genre -> genre == 37 || genre == 10759 || genre == 80 || genre == 18 || genre == 9648 } }
        } catch (e: Exception) {
            return listOf()
        }
    }
}