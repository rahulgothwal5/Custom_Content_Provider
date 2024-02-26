package com.example.customcontentprovider.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.customcontentprovider.data.localdb.ProductDB
import com.example.customcontentprovider.data.model.Product
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductContentProvider : ContentProvider() {

    lateinit var productDB: ProductDB

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface ExampleContentProviderEntryPoint {
        fun getProductDB(): ProductDB
    }

    // Define constants for the content provider
    companion object {
        private const val AUTHORITY = "com.example.customcontentprovider"
        private const val PATH_PRODUCTS = "products"
        private const val PRODUCT_DIR = 1
        private const val PRODUCT_ITEM = 2
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH_PRODUCTS, PRODUCT_DIR)
        addURI(AUTHORITY, "$PATH_PRODUCTS/#", PRODUCT_ITEM)
    }

    override fun onCreate(): Boolean {
        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(appContext, ExampleContentProviderEntryPoint::class.java)

        productDB = hiltEntryPoint.getProductDB()
        return true
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (uriMatcher.match(uri)) {
            PRODUCT_DIR -> {
                cursor = productDB.getProductDao().getAllProductsCursor()
            }
            PRODUCT_ITEM -> {
                val productId = uri.lastPathSegment?.toIntOrNull() ?: return null
                cursor = productDB.getProductDao().getProductCursor(productId)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null // We won't implement this in this example
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            PRODUCT_DIR -> {
                val product = Product(
                    category = values?.getAsString("category") ?: "",
                    description = values?.getAsString("description") ?: "",
                    id = values?.getAsInteger("id") ?: 0,
                    image = values?.getAsString("image") ?: "",
                    price = values?.getAsDouble("price") ?: 0.0,
                    title = values?.getAsString("title") ?: ""
                )
                GlobalScope.launch {
                    productDB.getProductDao().addProducts(listOf(product))
                }
                return Uri.withAppendedPath(uri, product.id.toString())
            }
            else -> throw IllegalArgumentException("Unsupported URI for insertion: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            PRODUCT_DIR -> {
                throw IllegalArgumentException("Invalid URI, please provide product ID: $uri")
            }
            PRODUCT_ITEM -> {
                val productId = uri.lastPathSegment?.toIntOrNull() ?: return 0
                GlobalScope.launch {
                    productDB.getProductDao().deleteProduct(productId)
                }
                1 // Successfully deleted
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            PRODUCT_DIR -> {
                throw IllegalArgumentException("Invalid URI, please provide product ID: $uri")
            }
            PRODUCT_ITEM -> {
                val productId = uri.lastPathSegment?.toIntOrNull() ?: return 0
                val updatedProduct = Product(
                    category = values?.getAsString("category") ?: "",
                    description = values?.getAsString("description") ?: "",
                    id = productId,
                    image = values?.getAsString("image") ?: "",
                    price = values?.getAsDouble("price") ?: 0.0,
                    title = values?.getAsString("title") ?: ""
                )
                GlobalScope.launch {
                    productDB.getProductDao().updateProduct(updatedProduct)
                }
                1 // Successfully updated
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}