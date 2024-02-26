package com.example.contentresolver_app.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.contentresolver_app.ui.model.Product
import com.example.contentresolver_app.viewmodel.ProductsListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun ProductListingScreen() {

    val productsListViewModel = hiltViewModel<ProductsListViewModel>()

    val productListState = productsListViewModel.productsList.collectAsState()
    val context = LocalContext.current


    when (productListState.value) {
        is ProductsListViewModel.ProductListUIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...", style = MaterialTheme.typography.headlineSmall)
            }
        }

        is ProductsListViewModel.ProductListUIState.Success -> {
            if ((productListState.value as ProductsListViewModel.ProductListUIState.Success).products.isEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Products Found", style = MaterialTheme.typography.bodySmall, color = Color.Red
                    )
                }
            }
            else{
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                ) {
                    val list = (productListState.value as ProductsListViewModel.ProductListUIState.Success).products
                    items(list.size) {
                        val item = list[it]
                        Log.d("DATA", Gson().toJson(item))
                        ProductItem(item = item)
                    }
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Something went wrong", style = MaterialTheme.typography.bodySmall, color = Color.Red
                )
            }
        }
    }

}


@Composable
fun ProductItem(item: Product) {
    Card(modifier = Modifier
        .clickable { }
        .padding(7.5.dp)
        .clip(RoundedCornerShape(10.dp))) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {

            Text(
                text = "Title : ${item.title}",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Category : ${item.category}",
                fontSize = 14.sp,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Price : ${item.price}",
                fontSize = 14.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${item.description}",
                fontSize = 12.sp,
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
@Preview
fun previewProductItem() {
    ProductItem(
        item = Product(
            category = "jewelery",
            description = "Classic Created Wedding Engagement Solitaire Diamond Promise Ring for Her. Gifts to spoil your love more for Engagement, Wedding, Anniversary, Valentine\u0027s Day...",
            id = 7,
            image = "https://fakestoreapi.com/img/71YAIFU48IL._AC_UL640_QL65_ML3_.jpg",
            price = 9.99,
            title = "White Gold Plated Princess"
        )
    )
}