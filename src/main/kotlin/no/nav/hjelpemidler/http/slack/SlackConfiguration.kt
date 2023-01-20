package no.nav.hjelpemidler.http.slack

interface SlackConfiguration {
    val slackWebHook: String
}

data class DefaultSlackConfiguration(
    override val slackWebHook: String,
) : SlackConfiguration
