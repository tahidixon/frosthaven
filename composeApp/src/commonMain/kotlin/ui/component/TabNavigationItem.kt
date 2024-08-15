package ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch

@Composable
fun TabNavigationItem(tab:Tab, modalDrawerState: DrawerState)
{
    Logger.w { "TabNavigationItem: $tab" }
    val tabNavigator = LocalTabNavigator.current
    val scope = rememberCoroutineScope()
    NavigationDrawerItem(
        label = { Text(tab.options.title) },
        icon = tab.options.icon?.let {
            {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = it,
                    contentDescription = null
                )
            } },
        selected = false,
        onClick = {
            tabNavigator.current = tab
            scope.launch{ modalDrawerState.close() }
        }
    )
}