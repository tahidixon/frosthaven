package core.models.turn

import core.models.actor.Actor

data class TurnDetails(
    val turnNumber: Int,
    val players: List<Actor>,
)
