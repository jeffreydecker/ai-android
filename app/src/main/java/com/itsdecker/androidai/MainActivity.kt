package com.itsdecker.androidai

import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.itsdecker.androidai.navigation.NavigationDestination
import com.itsdecker.androidai.screens.addmodel.AddModelScreen
import com.itsdecker.androidai.screens.main.MainScreen
import com.itsdecker.androidai.screens.SecondaryScreen
import com.itsdecker.androidai.screens.chat.ChatScreen
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidaiTheme(darkTheme = true) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestination.Main,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<NavigationDestination.Main> {
                            MainScreen(
                                onChatNowClick = {
                                    navigate(
                                        navController,
                                        NavigationDestination.Chat(),
                                    )
                                },
                                onAddModelClick = {
                                    navigate(
                                        navController,
                                        NavigationDestination.AddModel
                                    )
                                },
                            )
                        }
                        composable<NavigationDestination.AddModel> {
                            AddModelScreen(viewModel())
                        }
                        composable<NavigationDestination.Chat> {
                            ChatScreen(hiltViewModel())
                        }
                        composable<NavigationDestination.Secondary> {
                            val route = it.toRoute<NavigationDestination.Secondary>()
                            SecondaryScreen(route.selectedModel)
                        }
                    }
                }
            }
        }
    }

    private fun navigate(navController: NavController, destination: NavigationDestination) {
        navController.navigate(destination)
    }
}