package com.example.customcontentprovider.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.customcontentprovider.data.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDB : RoomDatabase() {
    abstract fun getProductDao(): ProductDAO
}