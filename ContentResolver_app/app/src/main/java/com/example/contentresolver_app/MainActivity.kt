package com.example.contentresolver_app

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contentresolver_app.ui.theme.ContentResolver_appTheme
import com.example.contentresolver_app.ui.ProductListingScreen
import com.example.contentresolver_app.ui.components.TopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContentResolver_appTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopBar()
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ProductListingScreen()
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    fun readDataFromContentProvider() {
// Define the URI to access the products data
        val uri = Uri.parse("content://com.example.customcontentprovider/products")

// Query the content provider for the products data
        val cursor = contentResolver.query(uri, null, null, null, null)

        val msg = if (cursor != null) "CURSOR is not nulllllllllllll" else "its null shit"

        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

// Iterate through the cursor to retrieve the products data
        cursor?.apply {
            while (moveToNext()) {
                val productId = getInt(getColumnIndex("id"))
                val category = getString(getColumnIndex("category"))
                val description = getString(getColumnIndex("description"))
                val image = getString(getColumnIndex("image"))
                val price = getDouble(getColumnIndex("price"))
                val title = getString(getColumnIndex("title"))
                // Process retrieved data
            }
            close()
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentResolver_appTheme {
        Greeting("Android")
    }
}