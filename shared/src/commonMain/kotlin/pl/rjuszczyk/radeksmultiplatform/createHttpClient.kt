package pl.rjuszczyk.radeksmultiplatform

import io.ktor.client.HttpClient
import kotlinx.serialization.UnstableDefault

@UnstableDefault
expect fun createHttpClient(): HttpClient