package com.exsite.meapmeap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exsite.meapmeap.detailscreen.MeapDetailScreen
import com.exsite.meapmeap.searchscreen.SearchScreen
import com.exsite.meapmeap.ui.theme.MeapMeapTheme

object Route {
    const val SEARCH_SCREEN = "searchScreen"
    const val MEAP_DETAIL_SCREEN = "meapDetailScreen/{id}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val topBar: @Composable (() -> Unit)? = when (currentRoute) {
        Route.MEAP_DETAIL_SCREEN -> {
            {
                CenterAlignedTopAppBar(
                    title = { Text("Meap Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        }

        else -> null
    }

    Scaffold(
        topBar = { topBar?.invoke() },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.SEARCH_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.SEARCH_SCREEN) {
                SearchScreen(
                    navigateToMeapDetail = { id ->
                        navController.navigate("meapDetailScreen/$id")
                    }
                )
            }
            composable(Route.MEAP_DETAIL_SCREEN) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: return@composable

                MeapDetailScreen(
                    id = id,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeapMeapTheme {
                App()
            }
        }
    }
}