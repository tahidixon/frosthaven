package core.http.tasks

import core.models.actor.toCreatableActor
import io.ktor.http.*
import kotlinx.serialization.encodeToString

class GetActorsTask : SyncTask() {
    override val method = HttpMethod.Get
    override val path = "$pathPrefix/players" // ToDo
}