package core.http.tasks

import io.ktor.http.*

class PostTurnCompleteTask : SyncTask() {
    override val method = HttpMethod.Post
    override val path = "$pathPrefix/gamestate/next-player"
}