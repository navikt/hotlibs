package no.nav.hjelpemidler.http.slack

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.Ignore

@Ignore("Brukes kun til å teste Slack-integrasjon under utvikling")
class SlackTest {
    private val slackWebHook = "<KREVES FOR TESTING MOT SLACK>"

    @Test
    fun `send testmelding til slack`() {
        runBlocking {
            val client = slack(DefaultSlackConfiguration(slackWebHook))
            client.sendMessage("hm-http", slackIconEmoji(":grimacing:"), "#digihot-brukers-hjelpemiddelside-dev", "TEST, ignorer meg! :waving-from-afar-right:")
            client.sendMessage("hm-http2", slackIconUrl("https://parade.com/.image/t_share/MTkwNTgxMTA1NjY0NDAyNTU3/funny-pictures.jpg"), "#digihot-brukers-hjelpemiddelside-dev", "TEST, ignorer meg! :waving-from-afar-right:")
        }
    }
}
