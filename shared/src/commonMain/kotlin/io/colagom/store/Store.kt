package io.colagom.store

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Event
interface State
interface Action

interface Store<STATE : State, ACTION : Action, EVENT : Event> {
    val state: StateFlow<STATE>
    val event: SharedFlow<EVENT>

    fun dispatch(action: ACTION)
}