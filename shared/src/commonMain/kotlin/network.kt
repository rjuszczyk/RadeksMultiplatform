import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun loadSomething(): String{
        try {
            val client = HttpClient() {
                install(JsonFeature) {
                    serializer = KotlinxSerializer(Json.nonstrict)
                }
            }
            val result = client.get<TestGetResponse>("https://httpbin.org/get")
            client.close()
            return result.url
        } catch (e: Throwable) {
            return e.message!!
        }
}

@Serializable
data class TestGetResponse(
    val url: String
)
