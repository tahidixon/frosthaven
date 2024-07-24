import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import core.app.initKoin
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screen.MainScreen
import ui.theme.AppTheme

@Composable
@Preview
fun App() {
    /*
    if (GlobalState.darkMode == null)
    {
        GlobalState.darkMode = isSystemInDarkTheme()
    }

    val darkMode = mutableStateOf(GlobalState.darkMode)
     */
    initKoin()
    AppTheme(darkTheme = isSystemInDarkTheme()) {
        Navigator(MainScreen())
    }
}