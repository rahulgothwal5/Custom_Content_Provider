package com.example.contentresolver_app.domain.repo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.example.contentresolver_app.ui.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ProductRepository {
    fun getProducts(): Flow<List<Product>?>
}

class ProductRepositoryImpl @Inject constructor(val context: Context) : ProductRepository {

    @SuppressLint("Range")
    override fun getProducts(): Flow<List<Product>?> {
        return flow {

            // Define the URI to access the products data
            val uri = Uri.parse("content://com.example.customcontentprovider/products")

            // Query the content provider for the products data
            val cursor = context.contentResolver.query(uri, null, null, null, null)

            val products = ArrayList<Product>()
            // Iterate through the cursor to retrieve the products data
            cursor?.apply {
                while (moveToNext()) {
                    val productId = getInt(getColumnIndex("id"))
                    val category = getString(getColumnIndex("category"))
                    val description = getString(getColumnIndex("description"))
                    val image = getString(getColumnIndex("image"))
                    val price = getDouble(getColumnIndex("price"))
                    val title = getString(getColumnIndex("title"))
                    products.add(
                        Product(
                            id = productId,
                            category = category,
                            description = description,
                            image = image,
                            price = price,
                            title = title
                        )
                    )
                    // Process retrieved data
                }
                close()
            }
            emit(products)
        }
    }
}
