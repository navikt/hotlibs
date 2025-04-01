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

/**
 * [PacketListener] for meldinger av type [T].
 *
 * Eksempel på implementasjon:
 * ```kotlin
 *     class SakOpprettetListener(connection: RapidsConnection) : KafkaMessageListener<SakOpprettetMessage>(
 *         SakOpprettetMessage::class,
 *         failOnError = true,
 *     ) {
 *         init {
 *             connection.register<SakOpprettetMessage>(this)
 *         }
 *
 *         override fun skipMessage(message: JsonMessage, context: MessageContext): Boolean = false
 *
 *         override suspend fun onMessage(message: SakOpprettetMessage, context: MessageContext) {
 *             // håndter melding her
 *         }
 *     }
 * ```
 *
 * @see [register]
 */
abstract class KafkaMessageListener<in T : KafkaMessage>(
    private val messageClass: KClass<T>,

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
        if (skipMessage(packet, context)) return
        val message = jsonMapper.readValue(packet.toJson(), messageClass.java)
        runBlocking(Dispatchers.IO) {
            onMessage(message, context)
        }
    }

    /**
     * Returner `true` hvis meldingen skal ignoreres.
     */
    abstract fun skipMessage(message: JsonMessage, context: MessageContext): Boolean

    abstract suspend fun onMessage(message: T, context: MessageContext)
}
