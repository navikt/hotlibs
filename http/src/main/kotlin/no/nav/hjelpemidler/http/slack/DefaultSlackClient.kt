package no.nav.hjelpemidler.http.slack

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory

internal class DefaultSlackClient(
    private val configuration: SlackConfiguration,
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
) : SlackClient {
    private val client: HttpClient = httpClientFactory {
        expectSuccess = true
    }

    override suspend fun sendMessage(
        username: String,
        icon: SlackIcon,
        channel: String,
        message: String,
    ) {
        // Compose JSON body
        val values = mutableMapOf(
            "text" to message,
            "channel" to channel,
            "username" to username,
        )

        when (icon.type) {
            SlackIconType.Emoji -> values["icon_emoji"] = icon.value
            SlackIconType.Url -> values["icon_url"] = icon.value
        }

        // Issue webhook
        client.post(configuration.slackWebHook) {
            contentType(ContentType.Application.Json)
            setBody(values)
        }
    }
}
