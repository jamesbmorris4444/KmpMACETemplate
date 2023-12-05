package com.jetbrains.handson.kmm.shared.network

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.jetbrains.handson.kmm.shared.entity.Movie
import com.jetbrains.handson.kmm.shared.entity.MoviesWithPageNumber
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json

class SpaceXApi {
    suspend fun getAllLaunches(): List<RocketLaunch> {
        val httpClient = HttpClient()
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            isLenient = false
        }
        val response: HttpResponse = httpClient.request("https://api.spacexdata.com/v5/launches") {
            method = HttpMethod.Get
        }
        val responseBody = response.bodyAsText()
        return jsonSerializer.decodeFromString(responseBody)
    }
}

class MoviePagingSource : PagingSource<Int, Movie>() {
    private val startingKey = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val key = params.key ?: startingKey
        return LoadResult.Page(
            data = getMovies(key),
            prevKey = when (key) {
                startingKey -> null
                else -> ensureValidKey(key = key - 1)
            },
            nextKey = key + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return startingKey
    }

    private fun ensureValidKey(key: Int) = kotlin.math.max(startingKey, key)

    private val baseUrl = "https://api.themoviedb.org/3/discover/movie?"
    private val pageRequestParam = "page"
    private val apiKeyRequestParam = "api_key"
    private val apiKey = "17c5889b399d3c051099e4098ad83493"
    private val languageRequestParam = "language"
    private val language = "en"

    private suspend fun getMovies(key: Int): List<Movie> {
        val httpClient = HttpClient()
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            isLenient = false
        }
        val response: HttpResponse = httpClient.request(
            "$baseUrl$apiKeyRequestParam=$apiKey&$languageRequestParam=$language&$pageRequestParam=$key"
//            The following construct results in an invalid URL failure
//            url {
//                protocol = URLProtocol.HTTPS
//                host = "api.themoviedb.org/3/discover"
//                path("movie")
//                parameters.append(pageRequestParam, "5")
//                parameters.append(apiKeyRequestParam, apiKey)
//                parameters.append(languageRequestParam, language)
//            }
        )
        val responseBody = response.bodyAsText()
        val moviesWithPageNumber: MoviesWithPageNumber =  jsonSerializer.decodeFromString(responseBody)
        return moviesWithPageNumber.results//.filter { it.genreIds.contains(37)}
    }
}