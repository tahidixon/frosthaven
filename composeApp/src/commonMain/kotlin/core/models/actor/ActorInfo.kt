package core.models.actor

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*
import core.models.game.GameStates
import core.models.rest.ErrorState
import ui.model.MainScreenModel

class ActorInfo(
    val state: State = State.CREATE,
    val name: String? = null,
    val initiative: Int? = null,
    val isMonster: Boolean = false) {

    private var isNameError: Boolean = false
    private var isInitiativeError: Boolean = false
    var errorState: ErrorState? = null

    enum class State {
        CREATE,
        SUBMIT_INITIATIVE,
        WAITING,
        ACTIVE
    }

    /*
    When creating -> All fields are enabled, icon is Create, delete action is active if not the last actor
    When submitting initiative -> Name field and monster are disabled, initiative is enabled, icon is Send
     */
    val icon: ImageVector = when (state)
    {
        State.CREATE -> FontAwesomeIcons.Solid.Plus
        State.SUBMIT_INITIATIVE -> FontAwesomeIcons.Solid.ArrowRight
        State.WAITING -> FontAwesomeIcons.Solid.Clock
        State.ACTIVE -> FontAwesomeIcons.Solid.Check
    }

    val isNameFieldEnabled: Boolean = state == State.CREATE

    val isInitiativeFieldEnabled: Boolean = state == State.CREATE || state == State.SUBMIT_INITIATIVE

    val isMonsterFieldEnabled: Boolean = state == State.CREATE

    val isPrimaryIconEnabled: Boolean = state in setOf(State.CREATE, State.SUBMIT_INITIATIVE, State.ACTIVE)

    val isDeleteIconEnabled: Boolean = state == State.SUBMIT_INITIATIVE

    fun handlePrimaryIconClick(screenModel: MainScreenModel, actor: Actor?) {
        when (state) {
            State.CREATE -> {
                if (actor == null && !name.isNullOrEmpty() && initiative.isValidInitiative()) {
                    screenModel.createActor(Actor(name = name, initiative = initiative, isMonster = isMonster))
                } else {
                    isNameError = name == null
                    isInitiativeError = initiative == null || !initiative.isValidInitiative()
                }
            }
            State.SUBMIT_INITIATIVE -> {
                if (name != null && initiative != null) {
                    screenModel.submitInitiative(ActorInitiative(name, initiative))
                } else {
                    isNameError = name == null
                    isInitiativeError = initiative == null
                }
            }
            State.ACTIVE -> {
                screenModel.submitTurnComplete()
            }
            else -> throw NotImplementedError()
        }
    }

    override fun toString(): String {
        return "ActorInfo(state=$state, name=$name, initiative=$initiative, isMonster=$isMonster)"
    }


}