package core.models.actor

import kotlinx.serialization.Serializable

@Serializable
data class ActorInitiative(
    val name: String,
    val initiative: Int
) {
    init {
        require(name.isNotEmpty())
        require((1..99).contains(initiative))
    }
}