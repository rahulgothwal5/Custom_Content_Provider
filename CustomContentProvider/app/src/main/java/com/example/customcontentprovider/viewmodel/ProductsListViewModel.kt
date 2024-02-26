package com.example.customcontentprovider.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customcontentprovider.data.model.Product
import com.example.customcontentprovider.domain.Result
import com.example.customcontentprovider.domain.usecases.AddProductsUseCase
import com.example.customcontentprovider.domain.usecases.GetProductsListUseCase
import com.example.customcontentprovider.viewmodel.ProductsListViewModel.ProductListUIState.Error
import com.example.customcontentprovider.viewmodel.ProductsListViewModel.ProductListUIState.Loading
import com.example.customcontentprovider.viewmodel.ProductsListViewModel.ProductListUIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
    private val addProductsUseCase: AddProductsUseCase
) : ViewModel() {

    private val _productsList: MutableStateFlow<ProductListUIState> = MutableStateFlow(Loading)
    val productsList = _productsList

    fun addProducts(products: List<Product>) {
        viewModelScope.launch {
            addProductsUseCase.invoke(products).collect { result ->
                delay(1000)
                fetchProducts()
            }
        }
    }

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