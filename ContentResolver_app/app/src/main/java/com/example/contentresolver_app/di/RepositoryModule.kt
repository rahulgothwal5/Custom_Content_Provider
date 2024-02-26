package com.example.contentresolver_app.di

import android.content.Context
import com.example.contentresolver_app.domain.repo.ProductRepository
import com.example.contentresolver_app.domain.repo.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideProductRepository(@ApplicationContext context: Context): ProductRepository {
        return ProductRepositoryImpl(context)
    }

}