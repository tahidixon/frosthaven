package core.http.tasks

import core.models.actor.ActorInitiative
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostActorInitiativeTask(initiative: ActorInitiative): SyncTask() {
    override val method = HttpMethod.Post
    override val path = "$pathPrefix/player/initiative" // ToDo
    override val body = Json.encodeToString(initiative)
}