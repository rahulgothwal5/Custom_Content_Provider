package com.example.customcontentprovider.domain.usecases

import com.example.customcontentprovider.data.model.Product
import com.example.customcontentprovider.domain.repo.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.customcontentprovider.domain.Result

@ViewModelScoped
class AddProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke(list: List<Product>) = flow {
        try {
            val result = repository.addProducts(list)
            if (result is Result.Success) {
                emit(Result.Success(result))
            } else {
                emit(Result.Error(Exception("Failed to fetch tweets for topic")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}