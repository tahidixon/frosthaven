package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import co.touchlab.kermit.Logger
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bell
import compose.icons.fontawesomeicons.solid.Plus
import org.koin.core.error.MissingPropertyException
import ui.component.Reminder
import ui.component.modal.AddReminderModal
import ui.model.RemindersTabModel

object RemindersTab: Tab {

    override val options: TabOptions
        @Composable
        get()
        {
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.Bell)
            return TabOptions(
                index = 1U,
                title = "Reminders",
                icon = icon
            )
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { RemindersTabModel() }
        Logger.i { "Reminders tab" }

        var activeModal by remember { mutableStateOf(screenModel.activeModal) }
        val sheetState = rememberModalBottomSheetState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                SmallFloatingActionButton(
                    onClick = { activeModal.value = RemindersTabModel.CREATION_MODAL_ID },
                    shape = RoundedCornerShape(8.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = FontAwesomeIcons.Solid.Plus,
                        contentDescription = "Add"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            RemindersContent(screenModel)
            if (activeModal.value != null) {
                ModalBottomSheet(
                    onDismissRequest = { activeModal.value = null },
                    sheetState = sheetState
                ) {
                    when (activeModal.value) {
                        RemindersTabModel.CREATION_MODAL_ID -> AddReminderModal(screenModel)
                        else -> throw MissingPropertyException("Expected a supported modal ID")
                    }
                }
            }
        }
    }

    @Composable
    private fun RemindersContent(screenModel: RemindersTabModel)
    {
        var reminderListState = rememberLazyListState()
        var reminders = remember { screenModel.reminders }

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Reminders is WIP - do not use",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = reminderListState,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                if (reminders.isNotEmpty()) {
                    reminders.forEach { reminder ->
                        item { Reminder(reminder, screenModel) }
                    }
                }
                else
                {
                    item { Text("No reminders to display") }
                }
            }
        }
    }
}