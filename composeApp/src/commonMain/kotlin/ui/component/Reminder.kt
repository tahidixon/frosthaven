package ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Bell
import compose.icons.fontawesomeicons.solid.Ghost
import compose.icons.fontawesomeicons.solid.Redo
import compose.icons.fontawesomeicons.solid.Trash
import core.models.reminder.Reminder
import core.models.reminder.ReminderInterval
import core.models.reminder.getIcon
import core.models.reminder.getState
import kotlinx.coroutines.launch
import ui.model.MainScreenModel
import ui.screen.constants.Sizes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Reminder(reminder: Reminder?, screenModel: MainScreenModel)
{
    var reminderState by remember { mutableStateOf(reminder.getState()) }
    var messageField by remember { mutableStateOf(reminder?.message) }
    var intervalField by remember { mutableStateOf(reminder?.interval?.turn) }
    var repeatField by remember { mutableStateOf(reminder?.interval?.isRecurring ?: false)}
    var reminderIcon = reminderState.getIcon()
    var scope = rememberCoroutineScope()

    SwipeBox(modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 50.dp,
        endContent = { swipeableState, endSwipeProgress ->
            SwipeIcon(
                imageVector = FontAwesomeIcons.Solid.Trash,
                contentDescription = "Delete",
                tint = Color.White,
                background = MaterialTheme.colorScheme.error,
                weight = 1f,
                iconSize = 20.dp
            ) {
                scope.launch {
                    swipeableState.animateTo(0)
                    screenModel.removeReminder(reminder)
                }
            }
        }) { _, _, _ ->
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            if (reminder != null && !reminder.isEditing)
            {
                Text(
                    modifier = Modifier.wrapContentHeight().align(Alignment.CenterVertically).weight(9f),
                    text = reminder.message,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                )
            }
            else
            {
                OutlinedTextField(
                    modifier = Modifier.weight(5f, true).fillMaxHeight(),
                    value = messageField ?: "",
                    enabled = true,
                    onValueChange = { content ->
                        messageField = content
                    },
                    label = {
                        Text(
                            text = "Message",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    singleLine = false,
                    maxLines = 2,
                    shape = MaterialTheme.shapes.large.copy(all = CornerSize(Sizes.CORNER_DEFAULT)),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.primary)
                )
                OutlinedTextField(
                    modifier = Modifier.weight(2f, true).fillMaxHeight().align(Alignment.CenterVertically),
                    value = intervalField?.toString() ?: "",
                    onValueChange = { content: String ->
                        intervalField = if (content.isNotEmpty() && content.toIntOrNull() != null) content.toInt() else null
                    },
                    enabled = reminder == null || reminder.isEditing,
                    label = {
                        Text(
                            text = "Interval",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    singleLine = true,
                    shape = MaterialTheme.shapes.large.copy(all = CornerSize(Sizes.CORNER_DEFAULT)),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                IconToggleButton(
                    modifier = Modifier.weight(1f, fill = false).align(Alignment.CenterVertically),
                    checked = repeatField,
                    enabled = reminder == null || reminder.isEditing,
                    onCheckedChange = { repeatField = it },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        checkedContentColor = Color.Green,
                        checkedContainerColor = Color(0x50FF00FF)
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = FontAwesomeIcons.Solid.Redo,
                        contentDescription = "Monster",
                        tint = if (repeatField) Color.Green else Color.Black
                    )
                }
            }

            IconButton(
                modifier = Modifier.weight(1f, true).height(50.dp).align(Alignment.CenterVertically),
                onClick = {
                    when (reminderState) {
                        Reminder.State.CREATE -> {
                            val interval = intervalField
                            if (interval != null)
                            {
                                screenModel.createReminder(
                                    Reminder(
                                        message = messageField!!,
                                        isAcknowledged = false,
                                        isEditing = false,
                                        interval = ReminderInterval(
                                            interval,
                                            repeatField
                                        )
                                    )
                                )
                            }
                        }
                        Reminder.State.UNACKNOWLEDGED -> {
                            reminder?.isAcknowledged = true
                        }
                        Reminder.State.ACKNOWLEDGED -> {
                            // Do nothing since should be disabled in this case
                        }
                    }
                },
                enabled = Reminder.State.ACKNOWLEDGED != reminderState && messageField != null
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = reminderIcon,
                    contentDescription = "Acknowledge reminder",
                    tint = when (reminderState)
                    {
                        Reminder.State.CREATE -> Color.Black
                        Reminder.State.UNACKNOWLEDGED -> MaterialTheme.colorScheme.error
                        Reminder.State.ACKNOWLEDGED -> Color.LightGray
                    }
                )
            }
        }
    }
}