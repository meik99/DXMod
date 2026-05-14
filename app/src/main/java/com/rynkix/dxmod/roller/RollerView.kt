package com.rynkix.dxmod.roller

import android.R.id.primary
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rynkix.dxmod.ui.theme.Green40
import com.rynkix.dxmod.ui.theme.Green80
import com.rynkix.dxmod.ui.theme.GreenGrey40
import com.rynkix.dxmod.ui.theme.GreenGrey80

val helpMessage = """
In the message box below, you can enter equations including die rolls that are calculated for you. This app supports D&D mechanics, including advantage and disadvantage. A basic equation like 2 + 5 * 2 will show a breakdown and the result (12). Die rolls use standard formatting: 1d20 means rolling one 20-sided die.

You can combine equations and die rolls for complex calculations using braces ( ), addition +, subtraction -, multiplication *, and division /. Append "a" or "d" to roll with advantage or disadvantage. For example, ((1d20 + d 2d(a 3d4) * 3) + 2) rolls 3d4 with advantage, then uses that result to roll disadvantage dice, adds 1d20, multiplies by 3, and adds 2.        
""".trimIndent()

@Preview(showBackground = true)
@Composable
fun RollerView(
    modifier: Modifier = Modifier
) {
    val chatHistory = remember { mutableStateListOf<String>(
        helpMessage
    ) }
    val chatText = remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    val chatTextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    )

    LaunchedEffect(chatHistory.size) {
        scrollState.scrollToItem(if (chatHistory.isEmpty()) 0 else chatHistory.size - 1)
    }

    return Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Chat history
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true),
            state = scrollState
        ) {
            itemsIndexed(chatHistory) { index, message ->
                SelectionContainer {
                    ChatBox(
                        text = message,
                        response = index % 2 == 0
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green80,
                focusedLabelColor = Green80
            ),
            label = { Text("Roll") },
            value = chatText.value,
            onValueChange = { chatText.value = it },
            singleLine = true,
            placeholder = { Text("2d20+5") },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                lineHeight = chatTextStyle.lineHeight
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    val inputText = chatText.value
                    chatHistory.add(inputText)
                    chatText.value = ""

                    val response = DiceRoller().ExecuteRollForResponse(inputText)
                        .joinToString("\n")
                    chatHistory.add(response)
                }
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserChatBox() {
    ChatBox(false)
}

@Preview(showBackground = true)
@Composable
fun AnswerChatBox() {
    ChatBox(true)
}

@Composable
fun ChatBox(
    response: Boolean = false,
    text: String = "Placeholder text"
) {
    Row {
        if (response) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = text,
            modifier = Modifier.weight(4f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (response) Green40 else GreenGrey40
                )
                .padding(8.dp)
        )

        if (!response) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }

}