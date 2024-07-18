package core.models.game

enum class GameStates(val pattern: Regex, val turnText: String)
{
    /*
    Gamestate::PreGame => "PRE_GAME".to_owned(),
			Gamestate::Standby => "STANDBY".to_owned(),
			Gamestate::InitiativeSubmit(i) => format!("INITIATIVE_SUBMIT_{}", i),
			Gamestate::PreTurn => "PRE_TURN".to_owned(),
			Gamestate::Turn(i) => format!("TURN_{}", i),
			Gamestate::PostTurn => "POST_TURN".to_owned(),
     */
    PRE_GAME(Regex("PRE_GAME"), "Waiting for game to start..."),
    STANDBY(Regex("STANDBY"), "Waiting for initiative submit..."),
    INITIATIVE_SUBMIT(Regex("INITIATIVE_SUBMIT_\\d+"), "Submit initiative!"),
    PRE_TURN(Regex("PRE_TURN"), "Starting turn..."),
    TURN(Regex("TURN_\\d+"), "Turn Active!"),
    POST_TURN(Regex("POST_TURN"), "Turn is over. Resolve any reminders...");
    companion object
    {
        fun getByResponse(responseBody: String): GameStates? {
            for (gameState in entries) {
                if (gameState.pattern.matches(responseBody))
                {
                    return gameState
                }
            }
            return null
        }
    }
}