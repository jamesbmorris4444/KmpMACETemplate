package com.jetbrains.handson.kmm.shared.network

import co.touchlab.kermit.Logger
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