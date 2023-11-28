package utils

import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts

object Utils {

    fun donorComparisonByString(donorWithProducts: Donor): String {
        return "${donorWithProducts.lastName},${donorWithProducts.dob}"
    }

    fun donorComparisonByStringWithProducts(donor: Donor): String {
        return "${donor.lastName},${donor.dob}"
    }

    fun donorBloodTypeComparisonByString(donorWithProducts: Donor): String {
        return donorWithProducts.aboRh
    }

    fun donorLastNameComparisonByString(donorWithProducts: Donor): String {
        return donorWithProducts.lastName
    }

    fun prettyPrintList(list: List<DonorWithProducts>) {
        Logger.i("MACELOG: =======================")
        list.forEach {
            Logger.i("MACELOG: donor and products=$it")
        }
        Logger.i("MACELOG: =======================")
    }

}