package core.models.actor

import kotlinx.serialization.Serializable

@Serializable
data class CreatableActor(
    val name: String,
    val initiative: Int,
    val is_monster: Boolean = false)