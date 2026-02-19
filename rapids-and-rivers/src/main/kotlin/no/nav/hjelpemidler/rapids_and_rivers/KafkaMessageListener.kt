package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.core.type.TypeReference
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
import no.nav.hjelpemidler.logging.teamInfo
import no.nav.hjelpemidler.serialization.jackson.jsonMapper

private val log = KotlinLogging.logger {}

/**
 * [PacketListener] for meldinger av type [T].
 *
 * Eksempel på implementasjon:
 * ```kotlin
 *     class SakOpprettetListener : KafkaMessageListener<SakOpprettetMessage>(failOnError = true) {
 *         override fun skipMessage(
 *             message: JsonMessage,
 *             context: ExtendedMessageContext,
 *             metadata: MessageMetadata,
 *             meterRegistry: MeterRegistry,
 *         ): Boolean = false
 *
 *         override suspend fun onMessage(
 *             message: Any,
 *             context: ExtendedMessageContext,
 *             metadata: MessageMetadata,
 *             meterRegistry: MeterRegistry,
 *         ) {
 *             // håndter melding her
 *         }
 *     }
 *
 *     RapidApplication.create(Configuration.current).apply {
 *         register(SakOpprettetListener())
 *     }
 * ```
 *
 * NB! Her arver vi fra [TypeReference] for å fange den generiske typen [T].
 *
 * @see [register]
 */
abstract class KafkaMessageListener<T : KafkaMessage>(
    /**
     * Settes til `true` hvis meldinger som ikke passerer validering skal få listener til å krasje.
     */
    private val failOnError: Boolean = false,
) : PacketListener, TypeReference<T>() {
    override fun onError(problems: MessageProblems, context: MessageContext, metadata: MessageMetadata) {
        log.info { "Validering av melding feilet, se Team Logs for detaljer" }
        log.teamInfo { "Validering av melding feilet: '${problems.toExtendedReport()}'" }
        if (failOnError) {
            error("Validering av melding feilet, se Team Logs for detaljer")
        }
    }

    override fun onPacket(
        packet: JsonMessage,
        context: MessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    ) {
        val messageContext = ExtendedMessageContext(context)
        if (skipMessage(packet, messageContext, metadata, meterRegistry)) {
            val eventId: String? = packet[KafkaMessage.EVENT_ID_KEY].textValue()
            val eventName: String? = packet[KafkaMessage.EVENT_NAME_KEY].textValue()
            log.info { "skipMessage() returnerte true, eventId: '$eventId', eventName: '$eventName'" }
            return
        }
        val message = jsonMapper.readValue(packet.toJson(), this)
        runBlocking(Dispatchers.IO) {
            onMessage(message, messageContext, metadata, meterRegistry)
        }
    }

    /**
     * Returner `true` hvis meldingen skal ignoreres.
     */
    abstract fun skipMessage(
        message: JsonMessage,
        context: ExtendedMessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    ): Boolean

    abstract suspend fun onMessage(
        message: T,
        context: ExtendedMessageContext,
        metadata: MessageMetadata,
        meterRegistry: MeterRegistry,
    )
}
