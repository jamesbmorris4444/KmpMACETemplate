
import androidx.paging.PagingSource
import app.cash.paging.PagingState
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.cache.Database
import com.jetbrains.handson.kmm.shared.cache.DatabaseDriverFactory
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

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Movie>): Int {
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