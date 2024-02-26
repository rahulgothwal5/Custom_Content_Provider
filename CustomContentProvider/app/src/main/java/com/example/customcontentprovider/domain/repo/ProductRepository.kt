package com.example.customcontentprovider.domain.repo

import com.example.customcontentprovider.data.datasource.DataSource
import com.example.customcontentprovider.domain.Result
import com.example.customcontentprovider.data.model.Product
import com.example.customcontentprovider.di.LocalDBDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>?>
    suspend fun addProducts(products:List<Product>): Result<Boolean>
}

class ProductRepositoryImpl @Inject constructor(
    @LocalDBDataSource
    private val localDBDataSource: DataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>?> {
        return localDBDataSource.fetchProductsFromDB()
    }

    override suspend fun addProducts(products:List<Product>): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
                val result = localDBDataSource.saveProductsToDB(products)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

}
