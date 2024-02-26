package com.example.customcontentprovider.data.datasource

import com.example.customcontentprovider.data.localdb.ProductDB
import com.example.customcontentprovider.data.model.Product
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


interface DataSource {
    fun fetchProductsFromDB(): Flow<List<Product>>
    suspend fun saveProductsToDB(data: List<Product>)
}

class LocalDBDataSourceImpl @Inject constructor(private val productDB: ProductDB) : DataSource {

    override fun fetchProductsFromDB(): Flow<List<Product>> {
        return productDB.getProductDao().getProducts()
    }

    override suspend fun saveProductsToDB(data: List<Product>) {
        productDB.getProductDao().addProducts(data)
    }
}