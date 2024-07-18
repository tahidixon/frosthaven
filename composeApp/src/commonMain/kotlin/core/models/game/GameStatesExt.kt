package core.models.game

fun GameStates.supportsCreating() = this in setOf(GameStates.INITIATIVE_SUBMIT)

fun GameStates.isTurnIndicator() = this in setOf(GameStates.INITIATIVE_SUBMIT)

fun GameStates.supportsInitiativeSubmit() = this in setOf(GameStates.INITIATIVE_SUBMIT)