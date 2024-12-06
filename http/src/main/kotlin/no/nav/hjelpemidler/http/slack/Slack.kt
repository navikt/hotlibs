package no.nav.hjelpemidler.http.slack

import io.ktor.client.engine.HttpClientEngine
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.http.DefaultHttpClientFactory
import no.nav.hjelpemidler.http.HttpClientFactory
import no.nav.hjelpemidler.http.createHttpClientFactory

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
    httpClientFactory: HttpClientFactory = DefaultHttpClientFactory,
    configuration: SlackConfiguration = slackEnvironmentConfiguration(),
): SlackClient = DefaultSlackClient(configuration = configuration, httpClientFactory = httpClientFactory)

/**
 * Creates the default slack client
 * @param configuration Specifies the webhook to use when sending messages to Slack. As a default the webhook is extracted
 *        from the env-var SLACK_HOOK (envFrom: hm-slack-hook)
 * @throws Exception
 */
fun slack(
    engine: HttpClientEngine,
    configuration: SlackConfiguration = slackEnvironmentConfiguration(),
): SlackClient = DefaultSlackClient(configuration = configuration, httpClientFactory = createHttpClientFactory(engine))
