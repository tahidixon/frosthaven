package core.http.tasks

import core.GlobalState
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

class GetCurrentTurnStateTask : SyncTask() {
    override val method = HttpMethod.Get
    override val path = "$pathPrefix/gamestate/current" // ToDo
    override val body = null
}