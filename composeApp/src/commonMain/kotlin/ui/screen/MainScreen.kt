package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*
import core.state.AppStateModel
import core.models.game.GameStates
import org.koin.core.error.MissingPropertyException
import ui.component.Actor
import ui.component.Reminder
import ui.component.TurnState
import ui.component.app.AppBar
import ui.model.MainScreenModel
import ui.screen.modal.SettingsModal

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { MainScreenModel() }.apply {
            start()
        }
        Logger.i { "MainScreen" }

        var activeModal by remember { mutableStateOf(screenModel.activeModal) }
        val navigationDrawerState = rememberDrawerState(DrawerValue.Closed)
        var turn by remember { mutableStateOf(screenModel.currentTurn)}
        val sheetState = rememberModalBottomSheetState()

        ModalNavigationDrawer(
            drawerState = navigationDrawerState,
            drawerContent = {
                // ToDo https://joelsblog.hashnode.dev/all-you-need-to-know-about-modal-navigation-drawers-in-jetpack-compose
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { AppBar { Text("Turn ${turn.value}")} },
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
            ) { paddingValues ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
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
                                MainScreenModel.SETTINGS_MODAL_ID -> drawSettingsModal()
                                else -> throw MissingPropertyException("Expected a supported modal ID")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ContentInternal(screenModel: MainScreenModel) {
        var turnState = remember { screenModel.turnState }

        Logger.i { "Redraw content internal. Game state: ${turnState.value}" }
        TurnState(turnState.value)
        TurnContainer(screenModel, turnState.value)

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
    private fun TurnContainer(screenModel: MainScreenModel, turnState: GameStates) {
        var actorsState = rememberLazyListState()
        var remindersState = rememberLazyListState()

        var actors = remember { screenModel.actors }
        var reminders = remember { screenModel.reminders }

        val isPostTurn = remember { mutableStateOf(turnState == GameStates.POST_TURN) }
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
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = if (isPostTurn.value) remindersState else actorsState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = CenterHorizontally
                ) {
                    if (isPostTurn.value)
                    {
                        reminders.forEach { reminder ->
                            item { Reminder(reminder, screenModel) }
                        }
                    }
                    else
                    {
                        actors.forEach { actor ->
                            item { Actor(screenModel, turnState, actor) }
                        }
                    }
                }
            }
        }
    }



    @Composable
    private fun drawSettingsModal()
    {
        SettingsModal().Content()
    }
}