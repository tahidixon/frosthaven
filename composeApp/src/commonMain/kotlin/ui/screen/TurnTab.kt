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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import co.touchlab.kermit.Logger
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*
import core.models.game.GameStates
import core.state.AppStateModel
import org.koin.core.error.MissingPropertyException
import ui.component.Actor
import ui.component.TurnState
import ui.model.TurnTabModel
import ui.screen.modal.ElementCreationModal

object TurnTab : Tab {
    override val options: TabOptions
        @Composable
        get()
        {
            val icon = rememberVectorPainter(FontAwesomeIcons.Solid.DiceD20)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Turn Details",
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { TurnTabModel() }.apply {
            start()
        }
        Logger.i { "Turn tab" }

        var activeModal by remember { mutableStateOf(screenModel.activeModal) }
        var appPadding by remember { mutableStateOf(AppStateModel.appPadding) }

        var turn by remember { mutableStateOf(screenModel.currentTurn)}
        val sheetState = rememberModalBottomSheetState()

        AppStateModel.topBarLabel.value = "Turn Details"

        Column(modifier = Modifier
            //.padding(top = appPadding.value.calculateTopPadding())
        )
        {
            TurnContent(screenModel)
            if (activeModal.value != null) {
                ModalBottomSheet(
                    onDismissRequest = { activeModal.value = null },
                    sheetState = sheetState
                ) {
                    when (activeModal.value) {
                        TurnTabModel.ELEMENT_CREATION_ID -> ElementCreationModal()
                        else -> throw MissingPropertyException("Expected a supported modal ID")
                    }
                }
            }
        }
    }

    @Composable
    private fun TurnContent(screenModel: TurnTabModel) {
        val turnState = remember { screenModel.turnState }

        Logger.i { "Redraw content internal. Game state: ${turnState.value}" }
        TurnState(turnState.value)
        TurnContainer(screenModel, turnState.value)

        SetupInitialActor(screenModel, turnState.value)
    }

    private fun SetupInitialActor(screenModel: TurnTabModel, turnState: GameStates)
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
    private fun TurnContainer(screenModel: TurnTabModel, turnState: GameStates) {
        var actorsState = rememberLazyListState()
        var actors = remember { screenModel.actors }
        Logger.i { "Redraw actor container. turn state: $turnState"}
        Scaffold(
            floatingActionButton = {
                SmallFloatingActionButton(
                    onClick = { screenModel.createActor(null) },
                    shape = RoundedCornerShape(8.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = FontAwesomeIcons.Solid.Plus,
                        contentDescription = "Add"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = actorsState,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = CenterHorizontally
                ) {
                    actors.forEach { actor ->
                        item { Actor(screenModel, turnState, actor) }
                    }
                }
            }
        }
    }
}