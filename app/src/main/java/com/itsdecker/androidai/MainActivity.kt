package com.itsdecker.androidai

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
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
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import com.itsdecker.androidai.screens.addkey.AddKeyScreen
import com.itsdecker.androidai.screens.main.MainScreen
import com.itsdecker.androidai.screens.chat.ChatScreen
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigator: Navigator

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidaiTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                ) { innerPadding ->
                    val navController = rememberNavController()
                    observeNavigation(navController)
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Main,
                        enterTransition = { slideInVertically(tween(durationMillis = 300), initialOffsetY = { fullHeight -> fullHeight}) },
                        exitTransition = { slideOutHorizontally(tween(durationMillis = 300), targetOffsetX = { fullWidth -> -fullWidth}) },
                        popEnterTransition = { slideInHorizontally(tween(durationMillis = 300), initialOffsetX = { fullWidth -> -fullWidth}) },
                        popExitTransition = { slideOutVertically(tween(durationMillis = 300), targetOffsetY = { fullHeight -> fullHeight}) },
                    ) {
                        composable<NavRoute.Main> {
                            MainScreen(
                                hiltViewModel(),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                        composable<NavRoute.AddKey> {
                            AddKeyScreen(
                                hiltViewModel(),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            )
                        }
                        composable<NavRoute.Chat> {
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