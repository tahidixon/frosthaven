package core.models.actor

import co.touchlab.kermit.Logger
import core.models.game.GameStates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun Actor.toActorName() : ActorName = ActorName(this.name)

fun Actor.toActorInitiative() : ActorInitiative = ActorInitiative(this.name, this.initiative)

fun Actor.toCreatableActor() : CreatableActor = CreatableActor(this.name, this.initiative, this.isMonster)

fun Actor?.getActorState(turnState: GameStates, activeActor: Actor?) : ActorInfo.State = when(turnState)
{
    GameStates.PRE_GAME -> ActorInfo.State.WAITING
    GameStates.STANDBY -> ActorInfo.State.WAITING
    GameStates.INITIATIVE_SUBMIT -> if (this == null) ActorInfo.State.CREATE else ActorInfo.State.SUBMIT_INITIATIVE
    GameStates.PRE_TURN -> ActorInfo.State.WAITING
    GameStates.TURN -> if (activeActor != null && this == activeActor) ActorInfo.State.ACTIVE else ActorInfo.State.WAITING
    GameStates.POST_TURN -> ActorInfo.State.WAITING
}

@OptIn(ExperimentalContracts::class)
fun Int?.isValidInitiative() : Boolean
{
    contract {
        returns (true) implies (this@isValidInitiative != null)
    }
    return this != null && (1..99).contains(this)
}