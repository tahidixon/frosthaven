package core.http.tasks

import core.models.actor.Actor
import core.models.actor.toCreatableActor
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CreatePlayerTask(player: Actor): SyncTask() {
    val format = Json { encodeDefaults = true }
    override val method = HttpMethod.Put
    override val path = "$pathPrefix/player" // ToDo
    override val body = format.encodeToString(player.toCreatableActor())
}