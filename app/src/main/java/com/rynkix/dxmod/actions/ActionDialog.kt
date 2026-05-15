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
    onAddAction: (Action) -> Unit = {},
    onDismissRequest: () -> Unit ={}
) {
    var description = remember { mutableStateOf("") }
    var equation = remember { mutableStateOf("") }
    var tags = remember { mutableStateOf("") }

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
                    text = "Add Action",
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
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (description.value.isEmpty() || equation.value.isEmpty()) {
                                return@Button
                            }

                            onAddAction(
                                Action(
                                    description = description.value,
                                    equation = equation.value,
                                    tags = tags.value.replace(" ", "").split(",", ";", "\n")
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