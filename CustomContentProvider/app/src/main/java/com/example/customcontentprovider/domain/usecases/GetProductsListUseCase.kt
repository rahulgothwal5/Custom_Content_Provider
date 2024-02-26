package com.example.customcontentprovider.domain.usecases

import com.example.customcontentprovider.domain.repo.ProductRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.customcontentprovider.domain.Result

@ViewModelScoped
class GetProductsListUseCase @Inject constructor(private val repository: ProductRepository) {
    operator fun invoke() = flow {
        try {
            repository.getProducts().collect{
                if (it != null) {
                    emit(Result.Success(it))
                } else {
                    emit(Result.Error(Exception("Failed to fetch tweets for topic")))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}