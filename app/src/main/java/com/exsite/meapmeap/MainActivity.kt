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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.exsite.meapmeap.detailscreen.MeapDetailScreen
import com.exsite.meapmeap.searchscreen.SearchScreen
import com.exsite.meapmeap.ui.theme.MeapMeapTheme
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel = viewModel<MainViewModel>()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isHome = currentRoute?.endsWith("Home") ?: false

    val topBar: @Composable (() -> Unit)? = when (isHome) {
        false -> {
            {
                CenterAlignedTopAppBar(
                    title = {
                        Text(viewModel.topBarTitle.value)
                    },
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
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Home> {
                SearchScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable<Screen.Detail> {
                val args = it.toRoute<Screen.Detail>()

                MeapDetailScreen(
                    id = args.id,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Serializable
sealed class Screen {
    @Serializable
    data object Home

    @Serializable
    data class Detail(val id: String)
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