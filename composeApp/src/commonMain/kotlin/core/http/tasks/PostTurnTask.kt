package core.http.tasks

import core.GlobalState
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

class PostTurnTask : SyncTask() {
    override val method = HttpMethod.Post
    override val path = "$pathPrefix/gamestate/next-player" // ToDo
    override val body = null
}

/*
ToDo
current flow:
Turn state is found in TurnState
Will be
Gamestate::PreGame => "PRE_GAME".to_owned(),
			Gamestate::Standby => "STANDBY".to_owned(),
			Gamestate::InitiativeSubmit(i) => format!("INITIATIVE_SUBMIT_{}", i),
			Gamestate::PreTurn => "PRE_TURN".to_owned(),
			Gamestate::Turn(i) => format!("TURN_{}", i),
			Gamestate::PostTurn => "POST_TURN".to_owned(),
While
 */