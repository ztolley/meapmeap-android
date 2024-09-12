package com.exsite.meapmeap.detailscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.exsite.meapmeap.MainViewModel
import com.exsite.meapmeap.api.Meap
import com.exsite.meapmeap.api.fetchMeap
import kotlinx.coroutines.launch

@Composable
fun MeapDetailScreen(
    id: String,
    navController: NavController,
    viewModel: MainViewModel
) {
    var meap by remember { mutableStateOf<Meap?>(null) }
    var loading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        viewModel.updateTopBarTitle("Meap Detail")

        coroutineScope.launch {
            meap = fetchMeap(id)
            loading = false
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    if (loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        meap?.let {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                MeapDetailSection(meap = it)
            }
        }
    }
}