package com.rynkix.dxmod.roller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rynkix.dxmod.ui.theme.Green40
import com.rynkix.dxmod.ui.theme.Green80
import com.rynkix.dxmod.ui.theme.GreenGrey40


@Preview(showBackground = true)
@Composable
fun RollerScreen(
    modifier: Modifier = Modifier,
    viewModel: RollerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Chat history
        ChatHistory(uiState.chatHistory)
        ChatBox(viewModel::processInput)
    }
}

@Composable
private fun ColumnScope.ChatHistory(
    chatHistory: List<String>
) {
    val scrollState = rememberLazyListState()

    LaunchedEffect(chatHistory.size) {
        scrollState.scrollToItem(if (chatHistory.isEmpty()) 0 else chatHistory.size - 1)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, true),
        state = scrollState
    ) {
        itemsIndexed(chatHistory) { index, message ->
            SelectionContainer {
                MessageBox(
                    text = message,
                    response = index % 2 == 0
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserMessageBox() {
    MessageBox(false)
}

@Preview(showBackground = true)
@Composable
fun AnswerMessageBox() {
    MessageBox(true)
}

@Composable
fun MessageBox(
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

@Composable
fun ChatBox(processInput: (String) -> Unit) {
    val chatText = remember { mutableStateOf("") }
    val chatTextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    )

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
                chatText.value = ""

                processInput(
                    inputText
                )
            }
        ),
    )
}