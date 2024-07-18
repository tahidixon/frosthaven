package core.models.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val id: Int? = null,
    val name: String,
    val initiative: Int,
    @SerialName("is_monster") val isMonster: Boolean = false,
    @SerialName("turn_complete") val turnComplete: Boolean? = null,
)
{
    init
    {
        require(initiative.isValidInitiative())
        require(name.isNotEmpty())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Actor

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}