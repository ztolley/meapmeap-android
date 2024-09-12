package com.exsite.meapmeap.searchscreen

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.exsite.meapmeap.MainViewModel
import com.exsite.meapmeap.R
import com.exsite.meapmeap.Screen
import com.exsite.meapmeap.api.SearchResult
import com.exsite.meapmeap.api.search
import com.exsite.meapmeap.ui.theme.MeapMeapTheme
import kotlinx.coroutines.launch


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    initialSearchResults: List<SearchResult> = emptyList(),
    navController: NavController,
    viewModel: MainViewModel
) {


    var query by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf(initialSearchResults) }
    var loading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val image = painterResource(id = R.drawable.meap_case)

    viewModel.updateTopBarTitle("Search")

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
            value = query,
            onValueChange = { query = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                searchResults = emptyList()
                loading = true
                focusManager.clearFocus()
                coroutineScope.launch {
                    searchResults = search(query)
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
                    SearchResultCard(
                        navigateToMeapDetail = { id ->
                            navController.navigate(Screen.Detail(id))
                        },
                        result = result
                    )
                }
            }
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
            distance = 1.2321
        ),
        SearchResult(
            id = "2",
            locationName = "Green Park",
            address = "456 Green Rd",
            postcode = "GR3 4CD",
            latitude = "51.5074",
            longitude = "-0.1278",
            distance = 2.5123
        ),
        SearchResult(
            id = "3",
            locationName = "Red Restaurant",
            address = "789 Red Ave",
            postcode = "RD5 6EF",
            latitude = "51.5074",
            longitude = "-0.1278",
            distance = 3.832123
        ),
        SearchResult(
            id = "1",
            locationName = "Blue Cafe",
            address = "123 Blue St",
            postcode = "BL1 2AB",
            latitude = "51.5074",
            longitude = "-0.1278",
            distance = 1.2321
        ),
        SearchResult(
            id = "2",
            locationName = "Green Park",
            address = "456 Green Rd",
            postcode = "GR3 4CD",
            latitude = "51.5074",
            longitude = "-0.1278",
            distance = 2.5123
        ),
        SearchResult(
            id = "3",
            locationName = "Red Restaurant",
            address = "789 Red Ave",
            postcode = "RD5 6EF",
            latitude = "51.5074",
            longitude = "-0.1278",
            distance = 3.832123
        )
    )
}

@Preview(showBackground = true, name = "Text preview", showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    val fakeResults = generateFakeSearchResults()
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()

    MeapMeapTheme {
        SearchScreen(
            initialSearchResults = fakeResults,
            navController = navController,
            viewModel = viewModel
        )
    }
}