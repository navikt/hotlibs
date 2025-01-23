package no.nav.hjelpemidler.http.slack

data class SlackMessage(
    val username: String,
    val icon: SlackIcon,
    val channel: String,
    val message: String,
)
