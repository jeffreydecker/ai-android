package com.itsdecker.androidai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itsdecker.androidai.navigation.NavigationDestination
import com.itsdecker.androidai.navigation.Navigator
import com.itsdecker.androidai.screens.addmodel.AddModelScreen
import com.itsdecker.androidai.screens.main.MainScreen
import com.itsdecker.androidai.screens.chat.ChatScreen
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigator: Navigator

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
                    observeNavigation(navController)
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestination.Main,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<NavigationDestination.Main> {
                            MainScreen(hiltViewModel())
                        }
                        composable<NavigationDestination.AddModel> {
                            AddModelScreen(hiltViewModel())
                        }
                        composable<NavigationDestination.Chat> {
                            ChatScreen(hiltViewModel())
                        }
                    }
                }
            }
        }
    }

    private fun observeNavigation(navController: NavController) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                navigator.navigationEvent.collect { navigationEvent ->
                    when (navigationEvent) {
                        Navigator.Event.GoBack -> navController.navigateUp()
                        is Navigator.Event.NavigateTo -> navController.navigate(navigationEvent.destination)
                    }
                }
            }
        }
    }
}