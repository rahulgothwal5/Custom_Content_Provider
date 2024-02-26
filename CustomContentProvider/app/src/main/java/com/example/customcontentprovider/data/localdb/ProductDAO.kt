package com.example.customcontentprovider.data.localdb

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.customcontentprovider.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(products : List<Product>)

    @Query("SELECT * FROM Product")
    fun getProducts(): Flow<List<Product>>

    @Update
    suspend fun updateProduct(product: Product)

    @Query("Delete FROM Product WHERE id = :productId")
    suspend fun deleteProduct(productId: Int)

    @Query("SELECT * FROM Product WHERE id = :productId")
    fun getProductById(productId: Int): Flow<Product>

    @Query("SELECT * FROM Product")
    fun getAllProductsCursor(): Cursor

    @Query("SELECT * FROM Product WHERE id = :productId")
    fun getProductCursor(productId: Int): Cursor

}