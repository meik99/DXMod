package com.rynkix.dxmod.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rynkix.dxmod.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ActionUiState (
    val actions: List<Action> = listOf(
    )
)

class ActionViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ActionUiState())
    val uiState: StateFlow<ActionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateActions()
        }
    }

    fun addAction(action: Action) {
        viewModelScope.launch(Dispatchers.IO) {
            if(action.uid == null) {
                MainActivity.db.actionDao().insertAll(action)
            } else {
                MainActivity.db.actionDao().update(action)
            }

            updateActions()
        }
    }

    fun deleteAction(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            MainActivity.db.actionDao().deleteById(uid)
            updateActions()
        }
    }

    private fun updateActions() {
        val actions = MainActivity.db.actionDao().getAll()

        _uiState.update { currentState ->
            currentState.copy(
                actions = actions
            )
        }
    }
}