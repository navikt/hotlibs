package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.River.PacketListener
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.kafka.Event
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import kotlin.reflect.KClass

abstract class EventListener<T : Event>(private val eventClass: KClass<T>) : PacketListener {
    override fun onPacket(
        packet: JsonMessage,
        context: MessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    ) {
        if (skipEvent(packet, context)) return
        val event = jsonMapper.readValue(packet.toJson(), eventClass.java)
        runBlocking(Dispatchers.IO) {
            onEvent(event, context)
        }
    }

    abstract fun skipEvent(packet: JsonMessage, context: MessageContext): Boolean

    abstract suspend fun onEvent(event: T, context: MessageContext)
}
