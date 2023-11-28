package no.nav.hjelpemidler.http.slack

import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test

@Ignore("Brukes kun til å teste Slack-integrasjon under utvikling")
class SlackTest {
    private val slackWebHook = "<KREVES FOR TESTING MOT SLACK>"

    @Test
    fun `Send testmelding til slack`() = runTest {
        val client = slack(DefaultSlackConfiguration(slackWebHook))

        client.sendMessage(
            username = "hm-http",
            icon = slackIconEmoji(":grimacing:"),
            channel = "#digihot-brukers-hjelpemiddelside-dev",
            message = "TEST, ignorer meg! :waving-from-afar-right:"
        )

        client.sendMessage(
            username = "hm-http2",
            icon = slackIconUrl("https://parade.com/.image/t_share/MTkwNTgxMTA1NjY0NDAyNTU3/funny-pictures.jpg"),
            channel = "#digihot-brukers-hjelpemiddelside-dev",
            message = "TEST, ignorer meg! :waving-from-afar-right:"
        )
    }
}
