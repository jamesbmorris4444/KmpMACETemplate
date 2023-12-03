package com.jetbrains.handson.kmm.shared.entity

import DateTime
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RocketLaunch(
    @SerialName("flight_number")
    val flightNumber: Int,
    @SerialName("name")
    val missionName: String,
    @SerialName("date_utc")
    val launchDateUTC: String,
    @SerialName("details")
    val details: String?,
    @SerialName("success")
    val launchSuccess: Boolean?,
    @SerialName("links")
    val links: Links
) {
    @Contextual
    var launchDate = DateTime().getFormattedDate(launchDateUTC, "dd.MM.yyyy")
}

@Serializable
data class MoviesWithPageNumber(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<Movies>
)

@Serializable
data class Movies(
    @SerialName(value = "vote_count") var voteCount: Int = 0,
    @SerialName(value = "video") var video: Boolean = false,
    @SerialName(value = "vote_average") var voteAverage: Float = 0f,
    @SerialName(value = "title") var title: String = "",
    @SerialName(value = "popularity") var popularity: Float = 0f,
    @SerialName(value = "poster_path") var posterPath: String = "",
    @SerialName(value = "original_language") var originaLanguage: String = "",
    @SerialName(value = "original_title") var originalTitle: String = "",
    @SerialName(value = "backdrop_path") var backdropPath: String = "",
    @SerialName(value = "adult") var adult: Boolean = false,
    @SerialName(value = "overview") var overview: String = "",
    @SerialName(value = "release_date") var releaseDate: String = "",
)

@Serializable
data class Links(
    @SerialName("patch")
    val patch: Patch?,
    @SerialName("article")
    val article: String?
)

@Serializable
data class Patch(
    @SerialName("small")
    val small: String?,
    @SerialName("large")
    val large: String?
)

data class DonorWithProducts(
    val donor: Donor,
    val products: List<Product> = listOf()
)

