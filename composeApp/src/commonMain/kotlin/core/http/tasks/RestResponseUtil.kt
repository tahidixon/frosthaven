package core.http.tasks

import io.ktor.client.statement.*

suspend fun HttpResponse?.toLogString() = "HTTP Response (${this?.status ?: "null"}) {${this?.bodyAsText() ?: "null"}}"