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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Ghost
import compose.icons.fontawesomeicons.solid.Trash
import core.models.actor.Actor
import core.models.actor.ActorInfo
import core.models.actor.getActorState
import core.models.game.GameStates
import kotlinx.coroutines.launch
import ui.model.MainScreenModel
import ui.screen.constants.Sizes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Actor(screenModel: MainScreenModel, turnState: GameStates, actor: Actor? = null) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var nameField by remember { mutableStateOf(actor?.name) }
    var initiativeField by remember { mutableStateOf(actor?.initiative) }
    var monsterField by remember { mutableStateOf(actor?.isMonster ?: false) }

    var activePlayer by remember { mutableStateOf(screenModel.activeActor) }
    Logger.i { "Get actor state. Turn state: $turnState"}
    val actorState = actor.getActorState(turnState, activePlayer.value)
    Logger.i { "Actor state: $actorState"}
    val actorInfo = ActorInfo(
        state = actorState,
        name = nameField,
        initiative = initiativeField,
        isMonster = monsterField
    )
    Logger.i { "Actor info: ${actorInfo}" }
    var errorState = remember { actorInfo.errorState }

    val scope = rememberCoroutineScope()

    SwipeBox(modifier = Modifier.fillMaxWidth(),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = if (actorInfo.isDeleteIconEnabled) 50.dp else 0.dp,
        endContent = if (!actorInfo.isDeleteIconEnabled) null else { swipeableState, endSwipeProgress ->
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
                    screenModel.removeActor(actor)
                }
            }
        }) { _, _, _ ->
        if (errorState != null) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.CenterVertically, true),
            ) {
                Text(
                    text = "Error: ${errorState.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Start
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().height(70.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(3f, true).fillMaxHeight(),
                value = nameField ?: "",
                enabled = actorInfo.isNameFieldEnabled,
                onValueChange = { content ->
                    nameField = content
                },
                label = {
                    Text(
                        text = "Name",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,
                singleLine = true,
                shape = MaterialTheme.shapes.large.copy(all = CornerSize(Sizes.CORNER_DEFAULT)),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.primary)
            )
            OutlinedTextField(
                modifier = Modifier.weight(2f, true).fillMaxHeight().align(Alignment.CenterVertically),
                value = initiativeField?.toString() ?: "",
                onValueChange = { content: String ->
                    initiativeField = if (content.isNotEmpty() && content.toIntOrNull() != null) content.toInt() else null
                },
                enabled = actorInfo.isInitiativeFieldEnabled,
                label = {
                    Text(
                        text = "Initiative",
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
                checked = monsterField,
                enabled = actorInfo.isMonsterFieldEnabled,
                onCheckedChange = { monsterField = it },
                colors = IconButtonDefaults.iconToggleButtonColors(
                    checkedContentColor = Color.Magenta,
                    checkedContainerColor = Color(0x50FF00FF)
                )
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = FontAwesomeIcons.Solid.Ghost,
                    contentDescription = "Monster",
                    tint = if (monsterField) Color.Magenta else Color.Black
                )
            }

            IconButton(
                modifier = Modifier.weight(1f, true).height(50.dp).align(Alignment.CenterVertically),
                onClick = {
                    keyboardController?.hide()
                    actorInfo.handlePrimaryIconClick(screenModel, actor)
                },
                enabled = actorInfo.isPrimaryIconEnabled
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = actorInfo.icon,
                    contentDescription = "Add Actor",
                    tint = if (actorInfo.isPrimaryIconEnabled) Color.Black else Color.LightGray
                )
            }
        }
    }
}