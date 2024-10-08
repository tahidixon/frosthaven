package ui.model

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import core.http.tasks.*
import core.models.actor.Actor
import core.models.actor.ActorInitiative
import core.models.actor.ActorName
import core.models.game.GameStates
import core.models.game.isTurnIndicator
import core.models.reminder.Reminder
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class TurnTabModel : ScreenModel {

    companion object {
        const val SETTINGS_MODAL_ID = "Settings#MODAL_ID"
        const val ELEMENT_CREATION_ID = "ElementCreation#MODAL_ID"
        const val LIST_ACTORS = "Actors#LIST_ID"
        const val LIST_REMINDERS = "Reminders#LIST_ID"
    }
    var activeModal = mutableStateOf(null as String?)
    var turnState = mutableStateOf(GameStates.PRE_GAME)
    var currentTurn = mutableStateOf(0)
    var actors = mutableStateListOf<Actor?>()
    var reminders = mutableStateListOf<Reminder?>()
    var activeActor = mutableStateOf<Actor?>(null)
    var activePage = mutableStateOf<String>(LIST_ACTORS)

    fun start()
    {
        screenModelScope.launch {
            val idleDelay = 3000L
            val activeDelay = 1000L
            var delay = idleDelay
            while (true) {
                delay(delay)
                Logger.i { "Requesting state" }
                val currentStateResponse = GetCurrentTurnStateTask().request()
                if (currentStateResponse?.status == HttpStatusCode.OK) {
                    val bodyAsText = currentStateResponse.bodyAsText()
                    val state = GameStates.getByResponse(bodyAsText)
                    if (state?.isTurnIndicator() ?: false)
                    {
                        currentTurn.value = bodyAsText.split("_").last().toInt()
                    }
                    Logger.i { "Game state: $state, bodyAsText: $bodyAsText, current turn: $currentTurn" }
                    state?.let { turnState.value = it}
                }

                 if (turnState.value == GameStates.TURN) {
                     populateActorInfo()
                     delay = activeDelay
                 } else {
                     delay = idleDelay
                 }
            }
        }
    }

    fun createActor(actor: Actor?)
    {
        Logger.i { "Create actor" }
        if (actor != null)
        {
            screenModelScope.launch {
                Logger.i { "In launch" }

                val createPlayerTaskResponse = CreatePlayerTask(actor).request()
                if (createPlayerTaskResponse?.status == HttpStatusCode.OK)
                {
                    Logger.i { "Response ${createPlayerTaskResponse.bodyAsText()}" }
                    var createdActor: Actor = Json.decodeFromString(createPlayerTaskResponse.bodyAsText())
                    val existingActor = actors.find { it?.name == actor.name }
                    if (existingActor != null)
                    {
                        Logger.i { "Actor already exists. Updating..." }
                        actors[actors.indexOf(existingActor)] = createdActor
                    }
                    else
                    {
                        Logger.i { "Actor does not exist. Adding..." }
                        actors.add(createdActor)
                    }
                    actors.remove(null)
                    start()
                }
                else if (createPlayerTaskResponse?.status == HttpStatusCode.NoContent)
                {
                    Logger.i { "No response. Fetch actors and check"}
                    var actorListFetch = GetActorsTask().request()
                    if (actorListFetch?.status == HttpStatusCode.OK)
                    {
                        var actorList: Array<Actor> = Json.decodeFromString(actorListFetch.bodyAsText())
                        Logger.i { "Actor list: ${actorList}"}
                    }
                    Logger.w { "Bad Response: $createPlayerTaskResponse" }
                }
            }
        }
        else
        {
            Logger.i { "Adding actor $actor to list" }
            actors.add(null)
            Logger.i { "Actors: $actors"}
        }
    }

    fun createReminder(reminder: Reminder)
    {
        reminders.add(reminder)
        reminders.remove(null)
    }

    fun removeActor(deletableActor: Actor?) {
        deletableActor?.let { actor ->
            screenModelScope.launch {
                Logger.i { "Remove actor $actor" }
                val deleteResponse = DeletePlayerTask(ActorName(actor.name)).request()
                if (deleteResponse?.status == HttpStatusCode.NoContent) {
                    actors.remove(actor)
                    Logger.i { "Actors after removal: $actors"}
                }
            }
        }
    }

    fun removeReminder(reminder: Reminder?) {
        reminders.remove(reminder)
    }

    fun submitInitiative(initiative: ActorInitiative) {
        screenModelScope.launch {
            Logger.i { "Submit initiative" }
            val submitResponse = PostActorInitiativeTask(initiative).request()
            if (submitResponse?.status != HttpStatusCode.NoContent) {
                Logger.w { "Failed to submit initiative: ${submitResponse?.toLogString()}"}
            }
        }
    }

    fun submitTurnComplete() {
        screenModelScope.launch {
            Logger.i { "Submit turnComplete" }
            val submitResponse = PostTurnCompleteTask().request()
            if (submitResponse?.status == HttpStatusCode.NoContent) {
                activeActor.value = null // Let the game state sweep reset active player
            }
        }
    }

    // Run when GameStates ==
    fun populateActorInfo()
    {
        screenModelScope.launch {
            val actorsResponse = GetActorsTask().request()
            if (actorsResponse?.status == HttpStatusCode.OK) {
                Logger.i { " Actor response body: ${actorsResponse.bodyAsText()}"}
                val actorList: Array<Actor> = Json.decodeFromString(actorsResponse.bodyAsText())
                Logger.i { "Actor list: ${actorList}" }
                val firstActor = actorList.filter { it.turnComplete == false }.firstOrNull()
                Logger.i { "First actor turn incomplete: $firstActor" }
                activeActor.value = firstActor
                if (activeActor.value == null)
                {
                    Logger.w { "Active actor null after fetch. Actor List: $actorList"}
                }
            }
            else
            {
                Logger.w { "Error fetching actor info" }
            }
        }
    }

    fun showModal(modal: String)
    {
        activeModal.value = modal
    }
}