package ui.screen.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import core.state.AppStateModel

class SettingsModal: Screen {
    @Composable
    override fun Content() {
        val hostAddress = remember { AppStateModel.hostAddress }
        val hostPort = remember { AppStateModel.hostPort }
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = hostAddress.value,
                onValueChange = { AppStateModel.hostAddress.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Address") },
                singleLine = true
            )
            OutlinedTextField(
                value = hostPort.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty() && it.toIntOrNull() != null) AppStateModel.hostPort.value = it.toInt() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Port") },
                singleLine = true
            )
        }
    }
}