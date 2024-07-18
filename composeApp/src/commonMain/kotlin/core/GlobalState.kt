package core

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator

object GlobalState
{
    var darkMode by mutableStateOf(false)
    var address by mutableStateOf("192.168.0.29")

    @Composable
    fun drawSettingsModal(expanded: MutableState<String?>)
    {
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Address") },
                singleLine = true
            )
            /*
            Row {
                Text(
                    text = "Union: ${union?.uid ?: "Not set"}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
                OutlinedButton(
                    onClick = {
                        this@GlobalState.union = null
                        expanded.value = null
                        navigator.push(LandingScreen())
                    }) {
                    Text(
                        text = "Clear",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Row {
                Text(
                    text = "Dark Mode",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
                Switch(
                    checked = darkMode,
                    onCheckedChange = { GlobalState.darkMode = it }
                )
            }
            Text(
                text = "Language",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
                DropdownMenu(
                    expanded = isLanguageMenuExpanded,
                    onDismissRequest = { isLanguageMenuExpanded = false },
                ) {
                    Language.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.label,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            onClick = { GlobalState.language = it })
                    }
                }
            }
            */

        }
    }
}