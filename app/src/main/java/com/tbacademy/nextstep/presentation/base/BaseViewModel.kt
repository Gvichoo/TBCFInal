package com.tbacademy.nextstep.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<STATE, EVENT, EFFECT>(
    initialState: STATE
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<STATE> get() = _viewState

    private val _effects = MutableSharedFlow<EFFECT>()
    val effects get() = _effects.asSharedFlow()

    abstract fun obtainEvent(event: EVENT)

    suspend fun emitEffect(effect: EFFECT) {
        _effects.emit(effect)
    }

    protected fun updateState(editor: STATE.() -> STATE) {
        _viewState.value = editor(_viewState.value)
    }

}