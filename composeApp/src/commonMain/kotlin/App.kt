import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import core.app.initKoin
import core.state.AppStateModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.TabNavigationItem
import ui.component.app.AppBar
import ui.screen.RemindersTab
import ui.screen.SettingsTab
import ui.screen.TurnTab
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

    val navigationDrawerState = rememberDrawerState(DrawerValue.Closed)
    val topBarLabel = remember { AppStateModel.topBarLabel }
    AppTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBar(navigationDrawerState) { Text(topBarLabel.value) } }
        ) { paddingValues ->
            AppStateModel.appPadding.value = paddingValues

            Column(
                modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TabNavigator(TurnTab) {
                    ModalNavigationDrawer(
                        drawerState = navigationDrawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                TabNavigationItem(TurnTab, navigationDrawerState)
                                TabNavigationItem(RemindersTab, navigationDrawerState)
                                TabNavigationItem(SettingsTab, navigationDrawerState)
                            }
                        }
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}