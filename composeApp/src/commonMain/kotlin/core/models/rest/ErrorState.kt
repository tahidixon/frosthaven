package core.models.rest

data class ErrorState(
    val severity: ErrorSeverity,
    val message: String
)