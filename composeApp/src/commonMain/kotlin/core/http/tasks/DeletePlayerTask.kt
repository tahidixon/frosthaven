package core.http.tasks

import core.models.actor.ActorName
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DeletePlayerTask(name: ActorName) : SyncTask() {
    override val body: String? = Json.encodeToString(name)
    override val path: String = "$pathPrefix/player"
    override val method: HttpMethod = HttpMethod.Delete
}