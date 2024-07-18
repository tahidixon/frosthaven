package core.http

import co.touchlab.kermit.Logger
import core.http.tasks.SyncTask
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.qualifier

class RestClient {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    suspend fun get(task: SyncTask): HttpResponse {
        val request = getRequest(task)
        Logger.i("Request GET ${request.url} (${request.body})")
        val response = client.get(request)
        Logger.i("Response $response")
        return response
    }

    suspend fun post(task: SyncTask): HttpResponse {
        val request = getRequest(task)
        Logger.i("Request POST ${request.url} (${request.body})")
        val response = client.post(request)
        Logger.i("Response $response")
        return response
    }

    suspend fun put(task: SyncTask): HttpResponse {
        val request = getRequest(task)
        Logger.i("Request PUT ${request.url} (${request.body})")
        val response = client.put(request)
        Logger.i("Response ${response.status} (${response.bodyAsText()}")
        return response
    }

    suspend fun delete(task: SyncTask): HttpResponse
    {
        val request = getRequest(task)
        Logger.i("Request DELETE ${request.url} (${request.body})")
        val response = client.delete(request)
        Logger.i("Response ${response.status} (${response.bodyAsText()}")
        return response
    }

    companion object
    {
        val QUALIFIER = qualifier("RestClient.QUALIFIER")
    }

    private fun getRequest(task: SyncTask): HttpRequestBuilder {

        return HttpRequestBuilder().apply {
            url {
                protocol = task.protocol
                host = task.host
                port = task.port
                path(task.path)
            }
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "ktor client")
            }
            val body = task.body
            if (body != null)
            {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }
    val baseBuilder = HttpRequestBuilder {

    }
}