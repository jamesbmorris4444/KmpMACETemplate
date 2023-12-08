package com.jetbrains.handson.kmm.shared.cache

import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllDonors()
        }
    }

    internal fun getAllDonors(): List<Donor> {
        return dbQuery.selectAllDonorsInfo(::mapDonorSelecting).executeAsList()
    }

    private fun mapDonorSelecting(
        id: Long,
        lastName: String,
        firstName: String,
        middleName: String,
        branch: String,
        aboRh: String,
        dob: String,
        gender: Boolean
    ): Donor {
        return Donor(
            id = id,
            lastName = lastName,
            firstName = firstName,
            middleName = middleName,
            branch = branch,
            aboRh = aboRh,
            dob = dob,
            gender = gender
        )
    }

    internal fun createDonor(donors: List<Donor>) {
        dbQuery.transaction {
            donors.forEach { donor ->
                insertDonor(donor)
            }
        }
        //dumpDonorsAndProducts()
    }

    internal fun insertDonor(donor: Donor) {
        dbQuery.insertDonor(
            id = null,
            lastName = donor.lastName,
            middleName = donor.middleName,
            firstName = donor.firstName,
            branch = donor.branch,
            aboRh = donor.aboRh,
            dob = donor.dob,
            gender = donor.gender
        )
    }

    internal fun updateDonor(firstName: String, middleName: String, lastName: String, dob: String, aboRh: String, branch: String, gender: Boolean, id: Long) {
        dbQuery.updateDonor(firstName, middleName, lastName, dob, aboRh, branch, gender, id)
    }

    internal fun getDonors(lastName: String): List<Donor> {
        return dbQuery.selectDonorsInfo("$lastName%").executeAsList()
    }

    private fun createProduct(products: List<Product>) {
        dbQuery.transaction {
            products.forEach { product ->
                insertProduct(product)
            }
        }
    }

    private fun insertProduct(product: Product) {
        dbQuery.insertProduct(
            id = null,
            donorId = product.donorId,
            din = product.din,
            aboRh = product.aboRh,
            productCode = product.productCode,
            expirationDate = product.expirationDate
        )
    }

   fun getAllProducts(): List<Product> {
        return dbQuery.selectAllProductsInfo(::mapProductSelecting).executeAsList()
    }

    private fun mapProductSelecting(
        id: Long,
        donorId: Long,
        din: String,
        aboRh: String,
        productCode: String,
        expirationDate: String
    ): Product {
        return Product(
            id = id,
            donorId = donorId,
            din = din,
            aboRh = aboRh,
            productCode = productCode,
            expirationDate = expirationDate
        )
    }

    internal fun donorFromNameAndDateWithProducts(lastName: String, dob: String): DonorWithProducts? {
        val donors: List<Donor> = dbQuery.donorFromNameAndDateWithProducts(lastName, dob).executeAsList()
        return if (donors.size == 1) {
            val donor = donors[0]
            val products: List<Product> = getAllProducts()
            val donorProducts = products.filter { it.donorId == donor.id }
            DonorWithProducts(donor = donor, products = donorProducts)
        } else {
            null
        }
    }

    internal fun insertProductsIntoDatabase(products: List<Product>) {
        createProduct(products)
    }

    internal fun updateDonorIdInProduct(newValue: Long, id: Long) {
        dbQuery.updateDonorIdInProduct(newValue, id)
    }

    internal fun selectProductsList(donorId: Long): List<Product> {
        return dbQuery.selectProductsList(donorId).executeAsList()
    }

    private fun dumpDonorsAndProducts() {
        val result1 = getAllDonors()
        result1.forEach {
            Logger.i("MACELOG: DUMP DONORS      Donor=$it")
        }
        val result2 = getAllProducts()
        result2.forEach {
            Logger.i("MACELOG: DUMP PRODUCTS    Product=$it")
        }
    }


}