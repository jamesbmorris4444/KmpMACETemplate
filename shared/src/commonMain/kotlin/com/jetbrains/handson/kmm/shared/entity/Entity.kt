package com.jetbrains.handson.kmm.shared.entity

import com.mace.corelib.DateTime
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
    var launchDate = DateTime().getFormattedDate(launchDateUTC)
}

@Serializable
data class MoviesWithPageNumber(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<Movie>
)

@Serializable
data class Movie(
    @SerialName(value = "vote_count") var voteCount: Int = 0,
    @SerialName(value = "video") var video: Boolean = false,
    @SerialName(value = "vote_average") var voteAverage: Float = 0f,
    @SerialName(value = "title") var title: String? = "",
    @SerialName(value = "popularity") var popularity: Float = 0f,
    @SerialName(value = "poster_path") var posterPath: String? = "",
    @SerialName(value = "original_language") var originaLanguage: String? = "",
    @SerialName(value = "original_title") var originalTitle: String? = "",
    @SerialName(value = "backdrop_path") var backdropPath: String? = "",
    @SerialName(value = "adult") var adult: Boolean = false,
    @SerialName(value = "overview") var overview: String? = "",
    @SerialName(value = "release_date") var releaseDate: String? = "",
    @SerialName(value = "genre_ids") var genreIds: List<Int> = listOf(),
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

data class Donor(
    @SerialName(value = "id") var id: Long = 0,
    @SerialName(value = "vote_count") var voteCount: Int = 0,
    @SerialName(value = "video") var video: Boolean = false,
    @SerialName(value = "vote_average") var voteAverage: Float = 0f,
    @SerialName(value = "title") var lastName: String = "",
    @SerialName(value = "popularity") var popularity: Float = 0f,
    @SerialName(value = "poster_path") var firstName: String = "",
    @SerialName(value = "original_language") var middleName: String = "",
    @SerialName(value = "original_title") var branch: String = "",
    @SerialName(value = "backdrop_path") var aboRh: String = "",
    @SerialName(value = "adult") var gender: Boolean = false,
    @SerialName(value = "overview") var overview: String = "",
    @SerialName(value = "release_date") var dob: String = "",
//    @Ignore var inReassociate: Boolean = false
)


data class Product(
    @SerialName(value = "donor_id") var donorId: Long = 0,
    @SerialName(value = "din") var din: String = "",
    @SerialName(value = "abo_rh") var aboRh: String = "",
    @SerialName(value = "product_code") var productCode: String = "",
    @SerialName(value = "expiration_date") var expirationDate: String = "",
//    @Ignore var removedForReassociation: Boolean = false,
//    @Ignore var inReassociate: Boolean = false
)

data class DonorWithProducts(
    val donor: Donor,
    val products: List<Product> = listOf()
)

@Serializable
data class HotelDestinationId(
    @SerialName(value = "dest_id") val destId: String = "",
    @SerialName(value = "dest_type") val searchType: String = "",
    @SerialName(value = "name") val name: String = ""
)

@Serializable
data class HotelRegion(
    @SerialName(value = "result") val hotelResult: List<HotelResult> = listOf(),
)

@Serializable
data class HotelPrice(
    @SerialName(value = "all_inclusive_price") val hotelPrice: Double,
)

@Serializable
data class HotelResult(
    @SerialName(value = "hotel_name_trans") val hotelName: String = "",
    @SerialName(value = "main_photo_url") val photoUrl: String = "",
    @SerialName(value = "price_breakdown") val price: HotelPrice
)

