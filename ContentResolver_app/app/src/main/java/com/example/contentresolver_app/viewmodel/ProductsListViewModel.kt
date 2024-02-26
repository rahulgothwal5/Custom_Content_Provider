package com.example.contentresolver_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentresolver_app.ui.model.Product
import com.example.contentresolver_app.viewmodel.ProductsListViewModel.ProductListUIState.Loading
import com.example.contentresolver_app.domain.usecases.GetProductsListUseCase
import com.example.contentresolver_app.domain.Result
import com.example.contentresolver_app.viewmodel.ProductsListViewModel.ProductListUIState.Success
import com.example.contentresolver_app.viewmodel.ProductsListViewModel.ProductListUIState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
) : ViewModel() {

    private val _productsList: MutableStateFlow<ProductListUIState> = MutableStateFlow(Loading)
    val productsList = _productsList


    private fun fetchProducts() {
        viewModelScope.launch {
            getProductsListUseCase.invoke().collect { result ->
                when (result) {

                    is Result.Success -> {
                        _productsList.value = Success(result.data)
                    }

                    is Result.Error -> {
                        _productsList.value = Error(result.exception)
                    }
                }
            }
        }
    }

    init {
        fetchProducts()
    }

    sealed class ProductListUIState {
        data class Success(val products: List<Product>) : ProductListUIState()
        object Loading : ProductListUIState()
        data class Error(val exception: Exception) : ProductListUIState()
    }
}