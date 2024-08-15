package core.state

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

object AppStateModel
{
    var topBarLabel = mutableStateOf("Untitled")
    var appPadding = mutableStateOf(PaddingValues(0.dp))
    var hostAddress = mutableStateOf("192.168.0.29")
    var hostPort = mutableStateOf<Int>(8080)
}