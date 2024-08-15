package ui.component.modal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.model.RemindersTabModel
import ui.screen.constants.Sizes

@Composable
fun AddReminderModal(screenModel: RemindersTabModel)
{
    var repeatField by remember { mutableStateOf(false) }
    var intervalField by remember { mutableStateOf(null as Int?) }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
            IconToggleButton(
                checked = repeatField,
                onCheckedChange = {
                    repeatField = it
                },
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (repeatField) "Every" else "On")
                }
            }
            OutlinedTextField(
                modifier = Modifier.weight(3f, true).fillMaxHeight(),
                value = intervalField?.toString() ?: "",
                enabled = true,
                onValueChange = { content ->
                    intervalField = content.toIntOrNull()
                },
                label = {
                    Text(
                        text = "Name",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,
                singleLine = true,
                shape = MaterialTheme.shapes.large.copy(all = CornerSize(Sizes.CORNER_DEFAULT)),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.primary)
            )
            Text("Turn(s)...", style = MaterialTheme.typography.labelMedium)
        }
    }
}