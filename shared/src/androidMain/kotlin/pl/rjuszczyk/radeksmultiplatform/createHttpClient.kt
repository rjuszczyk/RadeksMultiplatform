package pl.rjuszczyk.radeksmultiplatform

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@UnstableDefault
actual fun createHttpClient(): HttpClient {
    return HttpClient(block = {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    })

}