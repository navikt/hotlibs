package no.nav.hjelpemidler.http.slack

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

internal class InMemorySlackClient private constructor(
    private val messages: Queue<SlackMessage>,
) : SlackClient, Collection<SlackMessage> by messages {
    constructor() : this(ConcurrentLinkedQueue<SlackMessage>())

    override suspend fun sendMessage(username: String, icon: SlackIcon, channel: String, message: String) {
        messages.add(SlackMessage(username, icon, channel, message))
    }

    override val history: Collection<SlackMessage> get() = this
}
