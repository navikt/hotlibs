package no.nav.hjelpemidler.joark

import de.slub.urn.NID_RFC8141
import de.slub.urn.NamespaceIdentifier
import de.slub.urn.URN
import de.slub.urn.URNSyntaxError
import no.nav.hjelpemidler.core.ValueType
import java.util.UUID

@JvmInline
value class EksternReferanseId(override val value: String) : ValueType<String> {
    init {
        require(value.length <= MAX_LENGTH && value matches PATTERN) {
            "'$value' er ikke en gyldig EksternReferanseId"
        }
    }

    override fun toString(): String = value

    val søknadId: UUID?
        get() {
            val urn = urnOfOrNull(value) ?: return try {
                UUID.fromString(value.take(36))
            } catch (_: IllegalArgumentException) {
                return null
            }
            if (urn.namespaceIdentifier() in NID_BEHOVSMELDING) {
                return UUID.fromString(urn.namespaceSpecificString().toString())
            }
            return null
        }

    val isBehovsmelding: Boolean get() = TODO()

    val isDigitalBehovsmeldingGosys: Boolean get() = TODO()

    val isDigitalBehovsmeldingHotsak: Boolean get() = TODO()

    companion object {
        internal const val MAX_LENGTH = 200
        internal val PATTERN = Regex("^[a-zA-Z0-9-._~!$&\"\\\\*+,;=:@]+$")

        internal val NID_BEHOVSMELDING_GOSYS: NamespaceIdentifier = NID_RFC8141("behovsmelding-gosys")
        internal val NID_BEHOVSMELDING_HOTSAK: NamespaceIdentifier = NID_RFC8141("behovsmelding-hotsak")
        internal val NID_BEHOVSMELDING = setOf(NID_BEHOVSMELDING_GOSYS, NID_BEHOVSMELDING_HOTSAK)

        /**
         * Journalpost for digital behovsmelding til behandling i Gosys / Infotrygd.
         */
        @Deprecated("")
        const val DIGITAL_BEHOVSMELDING_GOSYS = "HJE-DIGITAL-SOKNAD"

        /**
         * Journalpost for digital behovsmelding til behandling i Hotsak.
         */
        @Deprecated("")
        const val DIGITAL_BEHOVSMELDING_HOTSAK = "HOTSAK"

        /**
         * Journalpost overført fra Hotsak til Gosys for videre behandling i Gosys / Infotrygd.
         */
        @Deprecated("")
        const val OVERFØRING_GOSYS = "HOTSAK_TIL_GOSYS"

        fun behovsmeldingGosys(id: UUID): EksternReferanseId =
            EksternReferanseId("urn:$NID_BEHOVSMELDING_GOSYS:$id")

        fun behovsmeldingHotsak(id: UUID): EksternReferanseId =
            EksternReferanseId("urn:$NID_BEHOVSMELDING_HOTSAK:$id")

        internal fun urnOfOrNull(value: String): URN? = try {
            URN.rfc8141().parse(value)
        } catch (_: URNSyntaxError) {
            null
        }
    }
}
