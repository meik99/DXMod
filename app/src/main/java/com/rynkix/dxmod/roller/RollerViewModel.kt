package com.rynkix.dxmod.roller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val helpMessage = """
In the message box below, you can enter equations including die rolls that are calculated for you. This app supports D&D mechanics, including advantage and disadvantage. A basic equation like 2 + 5 * 2 will show a breakdown and the result (12). Die rolls use standard formatting: 1d20 means rolling one 20-sided die.

You can combine equations and die rolls for complex calculations using braces ( ), addition +, subtraction -, multiplication *, and division /. Append "a" or "d" to roll with advantage or disadvantage. For example, ((1d20 + d 2d(a 3d4) * 3) + 2) rolls 3d4 with advantage, then uses that result to roll disadvantage dice, adds 1d20, multiplies by 3, and adds 2.        
""".trimIndent()

data class RollerUiState(
    val chatHistory: List<String> = listOf<String>(helpMessage)
)

class RollerViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RollerUiState())
    val uiState: StateFlow<RollerUiState> = _uiState.asStateFlow()

    fun processInput(inputText: String) {
        val response = DiceRoller().ExecuteRollForResponse(inputText)
            .joinToString("\n")

        _uiState.update { currentState ->
            currentState.copy(
                chatHistory = currentState.chatHistory + inputText + response
            )
        }

    }
}