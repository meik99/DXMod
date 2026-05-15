package com.rynkix.dxmod.actions

import android.graphics.Rect
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rynkix.dxmod.R
import com.rynkix.dxmod.roller.DiceRoller
import com.rynkix.dxmod.ui.theme.Green40
import com.rynkix.dxmod.ui.theme.Green80

@Preview(showBackground = true)
@Composable
fun RoundedSearchBar(
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Green80
        ),
        value = "",
        onValueChange = { },
        singleLine = true,
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.search_24px),
                contentDescription = "Search icon"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ActionScreen(
    modifier: Modifier = Modifier,
    viewModel: ActionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.padding(16.dp),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Green40,
                onClick = {
                    showDialog.value = true
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.add_24px),
                    contentDescription = "Add action"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            RoundedSearchBar()

            if (uiState.actions.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No actions yet",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = rememberLazyListState()
                ) {
                    items(uiState.actions) { action ->
                        ActionItem(action)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            if (showDialog.value) {
                AddActionDialog(
                    onAddAction = viewModel::addAction,
                    onDismissRequest = { showDialog.value = false },
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ActionItem(
    action: Action = Action(
        description = "Quarterstaff",
        equation = "1d8 + 5",
        tags = listOf("attack", "melee", "action")
    )
) {
    val context = LocalContext.current
    var toast: Toast? = remember { Toast(context) }

    Button(
        onClick = {
            toast?.cancel()
            toast = Toast.makeText(
                context,
                DiceRoller().ExecuteRoll(action.equation).toString(),
                Toast.LENGTH_LONG)
            toast?.show()
        },
        modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(
                    text = action.description,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = action.equation)

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    action.tags.forEach { tag ->
                        Tag(tag)
                    }
                }

            }
        }
    }
}

@Composable
fun Tag(text: String) {
    Row(
        modifier = Modifier
            .padding(end = 2.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Text(
            text = text,
            modifier = Modifier
                .background(Color.Red)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            color = Color.White
        )
    }
}
