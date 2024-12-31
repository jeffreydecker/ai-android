package com.itsdecker.androidai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import com.itsdecker.androidai.navigation.NavRoute
import com.itsdecker.androidai.navigation.Navigator
import com.itsdecker.androidai.screens.apikeyform.ApiKeyFormScreen
import com.itsdecker.androidai.screens.chat.ChatScreen
import com.itsdecker.androidai.screens.conversations.ConversationsScreen
import com.itsdecker.androidai.screens.apikeyslist.ApiKeysListScreen
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            observeNavigation(navController)
            AndroidaiTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Chat(apiKeyId = null, conversationId = null),
                        enterTransition = {
                            slideInVertically(
                                tween(durationMillis = 300),
                                initialOffsetY = { fullHeight -> fullHeight })
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                tween(durationMillis = 300),
                                targetOffsetX = { fullWidth -> -fullWidth })
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                tween(durationMillis = 300),
                                initialOffsetX = { fullWidth -> -fullWidth })
                        },
                        popExitTransition = {
                            slideOutVertically(
                                tween(durationMillis = 300),
                                targetOffsetY = { fullHeight -> fullHeight })
                        },
                    ) {
                        composable<NavRoute.ApiKeys> {
                            ApiKeysListScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = hiltViewModel(),
                            )
                        }
                        composable<NavRoute.ApiKeyForm> {
                            ApiKeyFormScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = hiltViewModel (),
                            )
                        }
                        composable<NavRoute.Conversations> {
                            ConversationsScreen(
                                modifier = Modifier.padding(innerPadding),
                                viewModel = hiltViewModel())
                        }
                        composable<NavRoute.Chat> {
                            ChatScreen(
                                viewModel = hiltViewModel(),
                                apiKeysListViewModel = hiltViewModel(),
                            )
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