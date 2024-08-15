package ui.component.app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bars
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(drawerState: DrawerState, title: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        title = title,
        navigationIcon = {
            IconButton(
                onClick = { scope.launch { if (drawerState.isOpen) drawerState.close() else drawerState.open() } }
            ) {
                Icon(
                    modifier = Modifier.size(25.dp).rotate(if (drawerState.isOpen) 90f else 0f),
                    imageVector = FontAwesomeIcons.Solid.Bars,
                    contentDescription = "Menu"
                )
            }
        }
    )
}