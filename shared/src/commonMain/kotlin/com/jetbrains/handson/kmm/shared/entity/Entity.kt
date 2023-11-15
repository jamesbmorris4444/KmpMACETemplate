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

//@Serializable
//data class Donor(
//    var lastName: String = "",
//    var firstName: String = "",
//    var middleName: String = "",
//    var branch: String = "",
//    var aboRh: String = "",
//    var gender: Boolean = false,
//    var dob: String = "",
//    var inReassociate: Boolean = false
//)

//@Serializable
//data class Product(
//    var donorId: Long = 0,
//    @SerialName("din") var din: String = "",
//    @SerialName("abo_rh") var aboRh: String = "",
//    @SerialName("product_code") var productCode: String = "",
//    @SerialName("expiration_date") var expirationDate: String = "",
//    var removedForReassociation: Boolean = false,
//    var inReassociate: Boolean = false
//
//)

data class DonorWithProducts(
    val donor: Donor,
    val products: List<Product> = listOf()
)

