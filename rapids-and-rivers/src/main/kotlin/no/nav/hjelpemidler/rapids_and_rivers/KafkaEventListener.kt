package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers.River.PacketListener
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageMetadata
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageProblems
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.kafka.KafkaMessage
import no.nav.hjelpemidler.logging.secureLog
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import kotlin.reflect.KClass

private val log = KotlinLogging.logger {}

abstract class KafkaEventListener<T : KafkaMessage>(
    private val eventClass: KClass<T>,

    /**
     * Settes til `true` hvis meldinger som ikke passerer validering skal få listener til å krasje.
     */
    private val failOnError: Boolean = false,
) : PacketListener {
    override fun onError(problems: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        log.info { "Validering av melding feilet, se secureLog for detaljer" }
        secureLog.info { "Validering av melding feilet: '${problems.toExtendedReport()}'" }
        if (failOnError) {
            error("Validering av melding feilet, se secureLog for detaljer")
        }
    }

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
