import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun loadSomething(): String{
        try {
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
        }

        val r = client.post<String>("http://schematic-ipsum.herokuapp.com?n=2",
                block = {
                    this.body = "{\n" +
                            "  \"type\": \"object\",\n" +
                            "  \"properties\": {\n" +
                            "    \"id\": { \"type\": \"string\", \"ipsum\": \"id\" },\n" +
                            "    \"name\": { \"type\": \"string\", \"ipsum\": \"name\" },\n" +
                            "    \"email\": { \"type\": \"string\", \"format\": \"email\" },\n" +
                            "    \"bio\": { \"type\": \"string\", \"ipsum\": \"sentence\" }\n" +
                            "  }\n" +
                            "}"
                }
                )
            println(r)
//        val result = client.get<TestGetResponse>("https://httpbin.org/get")
        client.close()
        return r//result.url
    } catch (e: Throwable) {
        return e.message!!
    }
}

@Serializable
data class TestGetResponse(
    val url: String
)
