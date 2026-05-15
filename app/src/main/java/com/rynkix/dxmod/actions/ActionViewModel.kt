package com.rynkix.dxmod.actions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ActionUiState (
    val actions: List<Action> = listOf(
        Action(
            description = "Quarterstaff",
            equation = "1d8 + 5",
            tags = listOf("attack", "melee", "action")
        )
    )
)

class ActionViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ActionUiState())
    val uiState: StateFlow<ActionUiState> = _uiState.asStateFlow()

    fun addAction(action: Action) {
        _uiState.update { currentState ->
            currentState.copy(
                actions = currentState.actions + action
            )
        }
    }
}