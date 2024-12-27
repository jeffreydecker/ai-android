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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.itsdecker.androidai.screens.conversations.ConversationsScreen
import com.itsdecker.androidai.ui.theme.AndroidaiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigator: Navigator

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            observeNavigation(navController)
            AndroidaiTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
//                                Icon(
//                                    painter = painterResource(R.drawable.ic_launcher_foreground),
//                                    contentDescription = null,
//                                )
                            },
                            title = {}, // TODO - Set title in some cases?
                            actions = { // TODO - Handle clicks
                                IconButton(
                                    content = {
                                        Icon(
                                            imageVector = Icons.Rounded.Key,
                                            contentDescription = null,
                                        )
                                    },
                                    onClick = { navigator.navigateTo(NavRoute.Main) },
                                )
                                // TODO - Conditionally show settings
                                IconButton(
                                    content = {
                                        Icon(
                                            imageVector = Icons.Rounded.Settings,
                                            contentDescription = null,
                                        )
                                    },
                                    onClick = {},
                                )
                            },
                        )
                    },
                    floatingActionButton = {}, // TODO - Maybe use this instead of per screen?
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoute.Conversations(),
                        enterTransition = { slideInVertically(tween(durationMillis = 300), initialOffsetY = { fullHeight -> fullHeight}) },
                        exitTransition = { slideOutHorizontally(tween(durationMillis = 300), targetOffsetX = { fullWidth -> -fullWidth}) },
                        popEnterTransition = { slideInHorizontally(tween(durationMillis = 300), initialOffsetX = { fullWidth -> -fullWidth}) },
                        popExitTransition = { slideOutVertically(tween(durationMillis = 300), targetOffsetY = { fullHeight -> fullHeight}) },
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable<NavRoute.Main> {
                            MainScreen(viewModel = hiltViewModel())
                        }
                        composable<NavRoute.AddKey> {
                            AddKeyScreen(viewModel = hiltViewModel())
                        }
                        composable<NavRoute.Conversations> {
                            ConversationsScreen(viewModel = hiltViewModel())
                        }
                        composable<NavRoute.Chat> {
                            ChatScreen(viewModel = hiltViewModel())
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