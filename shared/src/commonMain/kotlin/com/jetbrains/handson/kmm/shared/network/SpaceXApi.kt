package com.jetbrains.handson.kmm.shared.network

import com.jetbrains.handson.kmm.shared.entity.Movies
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

    private val baseUrl = "https://api.themoviedb.org/3/discover/movie?"
    private val pageRequestParam = "page"
    private val pageNumber = 5
    private val apiKeyRequestParam = "api_key"
    private val apiKey = "17c5889b399d3c051099e4098ad83493"
    private val languageRequestParam = "language"
    private val language = "en"

    suspend fun getMovies(): List<Movies> {
        val httpClient = HttpClient()
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            isLenient = false
        }
        val response: HttpResponse = httpClient.request(
            "$baseUrl$apiKeyRequestParam=$apiKey&$languageRequestParam=$language&$pageRequestParam=$pageNumber"
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
        return moviesWithPageNumber.results
    }
}