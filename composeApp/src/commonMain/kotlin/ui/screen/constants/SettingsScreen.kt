package ui.screen.constants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import core.state.AppStateModel
import ui.model.MainScreenModel

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { MainScreenModel() }.apply {
            start()
        }
        drawSettingsModal()
    }

    @Composable
    private fun drawSettingsModal()
    {
        val hostAddress by remember { mutableStateOf(AppStateModel.hostAddress) }
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = hostAddress.value,
                onValueChange = {
                    AppStateModel.hostAddress = hostAddress
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Address") },
                singleLine = true
            )
        }
    }
}