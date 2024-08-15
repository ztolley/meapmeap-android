package com.exsite.meapmeap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exsite.meapmeap.ui.theme.MeapMeapTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

data class SearchResult(
    val id: String,
    val locationName: String,
    val address: String,
    val postcode: String,
    val latitude: String,
    val longitude: String,
    val what3words: String,
    val distance: Double
)

suspend fun search(query: String): List<SearchResult> {
    return withContext(Dispatchers.IO) {
        val url = URL("https://www.meapmeap.co.uk/api/search?query=$query")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(response)

        val results = mutableListOf<SearchResult>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val result = SearchResult(
                id = jsonObject.getString("id"),
                locationName = jsonObject.getString("locationName"),
                address = jsonObject.getString("address"),
                postcode = jsonObject.getString("postcode"),
                latitude = jsonObject.getString("latitude"),
                longitude = jsonObject.getString("longitude"),
                what3words = jsonObject.getString("what3words"),
                distance = jsonObject.getDouble("distance")
            )
            results.add(result)
        }
        results
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeapMeapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    initialSearchResults: List<SearchResult> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(initialSearchResults) }
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val image = painterResource(id = R.drawable.meap_case)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = image, contentDescription = "MeapMeap Logo")
        }


        // Search Box
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                searchResults = emptyList()
                loading = true
                focusManager.clearFocus()
                coroutineScope.launch {
                    searchResults = search(searchQuery)
                    loading = false
                }

            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        // If loading, display loading indicator
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        } else {
            // Display search results
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { result ->
                    SearchResultCard(result = result)
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(result: SearchResult, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = result.locationName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = result.address,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = result.postcode,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Distance: ${
                    String.format(
                        Locale.getDefault(),
                        "%.1f",
                        result.distance
                    )
                } km",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
    }
}

fun generateFakeSearchResults(): List<SearchResult> {
    return listOf(
        SearchResult(
            id = "1",
            locationName = "Blue Cafe",
            address = "123 Blue St",
            postcode = "BL1 2AB",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "blue.cafe.location",
            distance = 1.2321
        ),
        SearchResult(
            id = "2",
            locationName = "Green Park",
            address = "456 Green Rd",
            postcode = "GR3 4CD",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "green.park.location",
            distance = 2.5123
        ),
        SearchResult(
            id = "3",
            locationName = "Red Restaurant",
            address = "789 Red Ave",
            postcode = "RD5 6EF",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "red.restaurant.location",
            distance = 3.832123
        ),
        SearchResult(
            id = "1",
            locationName = "Blue Cafe",
            address = "123 Blue St",
            postcode = "BL1 2AB",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "blue.cafe.location",
            distance = 1.2321
        ),
        SearchResult(
            id = "2",
            locationName = "Green Park",
            address = "456 Green Rd",
            postcode = "GR3 4CD",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "green.park.location",
            distance = 2.5123
        ),
        SearchResult(
            id = "3",
            locationName = "Red Restaurant",
            address = "789 Red Ave",
            postcode = "RD5 6EF",
            latitude = "51.5074",
            longitude = "-0.1278",
            what3words = "red.restaurant.location",
            distance = 3.832123
        )
    )
}

@Preview(showBackground = true, name = "Text preview", showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val fakeResults = generateFakeSearchResults()

    MeapMeapTheme {
        MainScreen(initialSearchResults = fakeResults)
    }
}