package com.example.customcontentprovider.di

import com.example.customcontentprovider.data.datasource.DataSource
import com.example.customcontentprovider.domain.repo.ProductRepository
import com.example.customcontentprovider.domain.repo.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideProductRepository(
       @LocalDBDataSource
       localDBDataSource: DataSource,
    ): ProductRepository {
        return ProductRepositoryImpl(
            localDBDataSource = localDBDataSource
        )
    }

}