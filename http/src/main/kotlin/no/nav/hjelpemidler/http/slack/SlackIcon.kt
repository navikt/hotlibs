package no.nav.hjelpemidler.http.slack

enum class SlackIconType {
    Emoji,
    Url,
}

data class SlackIcon(val type: SlackIconType, val value: String)

fun slackIconEmoji(emoji: String) = SlackIcon(SlackIconType.Emoji, emoji)
fun slackIconUrl(url: String) = SlackIcon(SlackIconType.Url, url)
