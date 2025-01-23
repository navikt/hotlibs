package no.nav.hjelpemidler.http.slack

interface SlackClient {
    suspend fun sendMessage(
        username: String,
        icon: SlackIcon,
        channel: String,
        message: String,
    )

    val history: Collection<SlackMessage>
}
