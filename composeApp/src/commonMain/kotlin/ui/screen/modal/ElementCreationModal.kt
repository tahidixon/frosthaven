package ui.screen.modal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.game.Elements
import ui.component.Element

@Composable
fun ElementCreationModal()
{
    var frostChecked = remember { mutableStateOf(false) }
    var fireChecked = remember { mutableStateOf(false) }
    var windChecked = remember { mutableStateOf(false) }
    var plantChecked = remember { mutableStateOf(false) }
    var sunChecked = remember { mutableStateOf(false) }
    var darkChecked = remember { mutableStateOf(false) }
    var createEnabled = remember { mutableStateOf(
        frostChecked.value ||
                fireChecked.value ||
                windChecked.value ||
                plantChecked.value ||
                sunChecked.value ||
                darkChecked.value) }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Element creation is WIP - do not use",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Element(frostChecked, Elements.FROST)
            Element(fireChecked, Elements.FIRE)
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Element(windChecked, Elements.WIND)
            Element(plantChecked, Elements.PLANT)
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Element(sunChecked, Elements.SUN)
            Element(darkChecked, Elements.DARK)
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                enabled = createEnabled.value,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Create")
            }
        }
    }
}