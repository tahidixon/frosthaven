package ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import core.state.AppStateModel
import ui.model.TurnTabModel

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get()
        {
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.Cog)
            return TabOptions(
                index = 2U,
                title = "Settings",
                icon = icon
            )
        }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TurnTabModel() }.apply {
            start()
        }
        drawSettingsModal()
    }

    @Composable
    private fun drawSettingsModal()
    {
        val hostAddress by remember { mutableStateOf(AppStateModel.hostAddress) }
        val hostPort by remember { mutableStateOf(AppStateModel.hostPort) }
        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = hostAddress.value,
                onValueChange = { address ->
                    AppStateModel.hostAddress.value = address
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Address") },
                singleLine = true
            )
            OutlinedTextField(
                value = hostPort.value.toString(),
                onValueChange = { port ->
                    if (port.toIntOrNull() != null)
                    {
                        AppStateModel.hostPort.value = port.toInt()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Web Port") },
                singleLine = true
            )
        }
    }
}