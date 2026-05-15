package com.rynkix.dxmod.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rynkix.dxmod.ui.theme.Green80

@Preview(showBackground = true)
@Composable
fun AddActionDialog(
    action: Action? = null,
    onAddAction: (Action) -> Unit = {},
    onDeleteAction: (uid: Int) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val description = remember { mutableStateOf(action?.description ?: "") }
    val equation = remember { mutableStateOf(action?.equation ?: "") }
    val tags = remember { mutableStateOf(action?.tags?.joinToString(",") ?: "") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if(action?.uid == null) "Add Action" else "Edit Action",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    placeholder = {
                        Text("Quarterstaff")
                    },
                    label = {
                        Text("Name")
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = equation.value,
                    onValueChange = { equation.value = it },
                    placeholder = {
                        Text("1d8 + 3")
                    },
                    label = {
                        Text("Equation")
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = tags.value,
                    onValueChange = { tags.value = it },
                    placeholder = {
                        Text("attack,melee")
                    },
                    label = {
                        Text("Tags")
                    }
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    if (action?.uid != null) {
                        Button(
                            onClick = {
                                onDeleteAction(action.uid)
                                onDismissRequest()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("Delete")
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Button(
                        onClick = {
                            if (description.value.isEmpty() || equation.value.isEmpty()) {
                                return@Button
                            }

                            onAddAction(
                                Action(
                                    action?.uid,
                                    description.value,
                                    equation.value,
                                    tags.value.replace(" ", "").split(",", ";", "\n")
                                )
                            )
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green80
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}