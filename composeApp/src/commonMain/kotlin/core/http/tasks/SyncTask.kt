package core.http.tasks

import co.touchlab.kermit.Logger
import core.state.AppStateModel
import core.http.RestClient
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SyncTask : KoinComponent {
    val client by inject<RestClient>()
    val host = AppStateModel.hostAddress.value
    val pathPrefix = "/frosthaven" // ToDo
    val port = AppStateModel.hostPort.value
    val protocol = URLProtocol.HTTP
    open val body: String? = null
    abstract val path: String
    abstract val method: HttpMethod

    open suspend fun request() : HttpResponse?
    {
        try
        {
            val response = when (method) {
                HttpMethod.Get -> client.get(this)
                HttpMethod.Post -> client.post(this)
                HttpMethod.Put -> client.put(this)
                HttpMethod.Delete -> client.delete(this)
                else -> throw UnsupportedOperationException("Handling of this request type not yet supported")
            }
            return response
        }
        catch (e: Exception)
        {
            Logger.e { "Exception occurred: ${e.message}"}
        }
        return null
    }
}