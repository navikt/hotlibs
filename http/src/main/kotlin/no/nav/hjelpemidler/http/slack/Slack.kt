package no.nav.hjelpemidler.http.slack

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import no.nav.hjelpemidler.configuration.ClusterEnvironment
import no.nav.hjelpemidler.configuration.Environment
import no.nav.hjelpemidler.configuration.EnvironmentVariable

object SlackEnvironmentVariable {
    /**
     * The Slack-webhook is extracted from the environment variable SLACK_HOOK (envFrom: hm-slack-hook)
     */
    val SLACK_HOOK by EnvironmentVariable
}

fun slackEnvironmentConfiguration(): SlackConfiguration = DefaultSlackConfiguration(
    slackWebHook = SlackEnvironmentVariable.SLACK_HOOK,
)

/**
 * Creates the default slack client
 * @param configuration Specifies the webhook to use when sending messages to Slack. As a default the webhook is extracted
 *        from the env-var SLACK_HOOK (envFrom: hm-slack-hook)
 * @throws Exception
 */
fun slack(
    engine: HttpClientEngine = CIO.create(),
    configuration: SlackConfiguration = slackEnvironmentConfiguration(),
): SlackClient = if (Environment.current is ClusterEnvironment) {
    DefaultSlackClient(configuration = configuration, engine = engine)
} else {
    InMemorySlackClient()
}
