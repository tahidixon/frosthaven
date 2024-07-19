package ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import com.kevinnzou.swipebox.SwipeBox
import com.kevinnzou.swipebox.SwipeDirection
import com.kevinnzou.swipebox.widget.SwipeIcon
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*
import core.GlobalState
import core.models.actor.*
import core.models.game.GameStates
import core.models.game.supportsCreating
import core.models.game.supportsInitiativeSubmit
import core.models.rest.ErrorState
import core.models.turn.TurnState
import kotlinx.coroutines.launch
import org.koin.core.error.MissingPropertyException
import ui.model.MainScreenModel
import ui.screen.constants.Sizes

class MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { MainScreenModel() }.apply {
            start()
        }
        Logger.i { "MainScreen" }

        var activeModal by remember { mutableStateOf(screenModel.activeModal) }
        val sheetState = rememberModalBottomSheetState()

        Scaffold(
            floatingActionButton = {
                SmallFloatingActionButton(
                    onClick = { screenModel.showModal(MainScreenModel.SETTINGS_MODAL_ID) },
                    shape = RoundedCornerShape(8.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = FontAwesomeIcons.Solid.Cog,
                        contentDescription = "Settings"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ContentInternal(screenModel)
                if (activeModal.value != null) {
                    ModalBottomSheet(
                        onDismissRequest = { activeModal.value = null },
                        sheetState = sheetState
                    ) {
                        when (activeModal.value) {
                            MainScreenModel.SETTINGS_MODAL_ID -> drawSettingsModal(
                                activeModal = activeModal
                            )

                            else -> throw MissingPropertyException("Expected a supported modal ID")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ContentInternal(screenModel: MainScreenModel) {
        var controlledActors = remember { screenModel.actors }
        var turnState = remember { screenModel.turnState }

        Logger.i { "Redraw content internal. Game state: ${turnState.value}" }
        DrawTurnLabel(screenModel)
        DrawTurnState(turnState.value)
        DrawActorContainer(screenModel, turnState.value, controlledActors)

        SetupInitialActor(screenModel, turnState.value)
    }

    private fun SetupInitialActor(screenModel: MainScreenModel, turnState: GameStates)
    {
        when (turnState) {
            GameStates.INITIATIVE_SUBMIT ->
            {
                if (screenModel.actors.isEmpty())
                {
                    screenModel.createActor(null)
                }
            }
            else -> Unit
        }
    }

    @Composable
    private fun DrawTurnState(gameState: GameStates) {
        Logger.i { "Redraw turn state. State: $gameState" }
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = gameState.turnText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    private fun DrawActorContainer(screenModel: MainScreenModel, turnState: GameStates, actors: SnapshotStateList<Actor?>) {
        var actorsState = rememberLazyListState()
        Logger.i { "Redraw actor container. turn state: $turnState"}
        Scaffold(
            floatingActionButton = {
                SmallFloatingActionButton(
                    onClick = { screenModel.createActor(null) },
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
            floatingActionButtonPosition = FabPosition.Start
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = actorsState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = CenterHorizontally
                ) {
                    actors.forEach { actor ->
                        item { DrawActor(screenModel, turnState, actor) }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun DrawActor(screenModel: MainScreenModel, turnState: GameStates, actor: Actor? = null) {
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
                            text = screenModel.nameInputLabel.value,
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
                            text = screenModel.initiativeInputLabel.value,
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
                    onClick = { actorInfo.handlePrimaryIconClick(screenModel, actor) },
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

    @Composable
    private fun DrawTurnLabel(screenModel: MainScreenModel) {
        var turnLabel by remember { mutableStateOf(screenModel.turnLabel.value) }

        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = turnLabel,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    private fun DrawTurnWaiting(screenModel: MainScreenModel) {
        var turnWaitingLabel by remember { mutableStateOf(screenModel.turnWaitingLabel) }
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = turnWaitingLabel,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    @Composable
    private fun DrawActiveTurnDetails(screenModel: MainScreenModel) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello world! Active turn details")
        }
    }

    @Composable
    private fun drawSettingsModal(activeModal: MutableState<String?>)//, navigator: Navigator)
    {
        Column(modifier = Modifier.fillMaxSize()) {
            GlobalState.drawSettingsModal(activeModal)
        }
    }
}