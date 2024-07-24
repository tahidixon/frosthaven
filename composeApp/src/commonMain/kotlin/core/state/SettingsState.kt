package core.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SettingsState {
    var address by mutableStateOf("192.168.0.29")
}