package com.itsdecker.androidai.navigation

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * Navigator class that can be injected to allow navigation from view models.
 */
@ActivityRetainedScoped
class Navigator @Inject constructor() {

    private val _navigationEvent = Channel<Event>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun navigateTo(destination: NavRoute) {
        _navigationEvent.trySend(Event.NavigateTo(destination))
    }

    fun navigateUptTo(destination: NavRoute) {
        _navigationEvent.trySend(Event.NavigateUpTo(destination))
    }

    fun goBack() = _navigationEvent.trySend(Event.GoBack)

    sealed interface Event {
        data class NavigateTo(val destination: NavRoute) : Event
        data class NavigateUpTo(val destination: NavRoute) : Event
        data object GoBack : Event
    }
}