package ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.game.Elements

@Composable
fun Element(checkState: MutableState<Boolean>, element: Elements) {
    IconToggleButton(
        checked = checkState.value,
        onCheckedChange = {
            checkState.value = it
        },
        modifier = Modifier
    ) {
        Column {
            Icon(
                imageVector = element.iconResource,
                contentDescription = null,
                tint = Color.Black
            )
            Text(element.label)
        }
    }
}