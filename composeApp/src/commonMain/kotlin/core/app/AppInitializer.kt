package core.app

import core.http.RestClient
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val appModules = listOf(
    module {
        single { RestClient() } withOptions {
            qualifier = RestClient.QUALIFIER
            createdAtStart()
        }
    }
)

fun initKoin() {
    startKoin {
        modules(appModules)
    }
}