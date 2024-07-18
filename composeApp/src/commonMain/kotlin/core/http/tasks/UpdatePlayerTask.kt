package core.http.tasks

import core.models.actor.Actor
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UpdatePlayerTask(player: Actor) : SyncTask() {
    override val method = HttpMethod.Put
    override val path = "$pathPrefix/player" // ToDo
    override val body = Json.encodeToString(player)
}