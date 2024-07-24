package core.state

import androidx.compose.material3.*
import androidx.compose.runtime.*
import core.models.app.AppPage

object AppStateModel
{
    var hostAddress = mutableStateOf("192.168.0.29")
    var hostPort = mutableStateOf<Int>(8080)
}