package com.example.customcontentprovider.di

import com.example.customcontentprovider.data.datasource.DataSource
import com.example.customcontentprovider.data.datasource.LocalDBDataSourceImpl
import com.example.customcontentprovider.data.localdb.ProductDB
import com.example.customcontentprovider.di.LocalDBDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @LocalDBDataSource
    fun providesLocalDBDataSource(fakerDB: ProductDB): DataSource {
        return LocalDBDataSourceImpl(fakerDB)
    }
}