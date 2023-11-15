package utils

import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts

object Utils {

    fun donorComparisonByString(donorWithProducts: Donor): String {
        return "${donorWithProducts.lastName},${donorWithProducts.dob}"
    }

    fun donorComparisonByStringWithProducts(donorWithProducts: DonorWithProducts): String {
        return "${donorWithProducts.donor.lastName},${donorWithProducts.donor.dob}"
    }

    fun donorBloodTypeComparisonByString(donorWithProducts: Donor): String {
        return donorWithProducts.aboRh
    }

    fun donorLastNameComparisonByString(donorWithProducts: Donor): String {
        return donorWithProducts.lastName
    }

    fun prettyPrintList(list: List<DonorWithProducts>) {
        Logger.d("MACELOG: =======================")
        list.forEach {
            Logger.d("MACELOG: donor and products=$it")
        }
        Logger.d("MACELOG: =======================")
    }

}