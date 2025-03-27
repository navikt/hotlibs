package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.hjelpemidler.configuration.ApplicationId
import no.nav.hjelpemidler.configuration.ResourceId
import java.net.URI

/**
 * Naiv implementasjon av URN basert på [URI].
 *
 * @see <a href="https://en.wikipedia.org/wiki/Uniform_Resource_Name">Uniform Resource Name</a>
 */
class URN private constructor(private val uri: URI) {
    @JsonCreator
    constructor(uri: String) : this(URI(uri))

    constructor(nid: String, vararg nss: String) : this("$SCHEME:$nid:${nss.joinToString(":")}")

    val nid: String get() = uri.schemeSpecificPart.substringBefore(':')
    val nss: String get() = uri.schemeSpecificPart.substringAfter(':')

    // START extensions

    constructor(applicationId: ApplicationId, resource: String, id: String) :
            this(applicationId.application, resource, id)

    constructor(applicationId: ApplicationId, resourceId: ResourceId, id: String) :
            this(applicationId, resourceId.resource, id)

    val application: String by this::nid
    val resource: String get() = nss.substringBeforeLast(':')
    val id: String get() = nss.substringAfterLast(':')

    fun validerEksternReferanseId() {
        val value = toString()
        check(value matches EKSTERN_REFERANSE_ID_REGEX) {
            "'$value' er ikke en gyldig eksternReferanseId, verdien samsvarer ikke med mønsteret: '$EKSTERN_REFERANSE_ID_REGEX'"
        }
    }

    // STOP extensions

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as URN
        return uri == other.uri
    }

    override fun hashCode(): Int = uri.hashCode()

    @JsonValue
    override fun toString(): String = uri.toString()

    fun toURI(): URI = uri

    companion object {
        const val SCHEME = "urn"

        /**
         * Hentet herfra: [CommonValidator.java](https://github.com/navikt/dokarkiv/blob/master/journalpost/src/main/java/no/nav/dokarkiv/journalpost/v1/validators/CommonValidator.java)
         */
        private val EKSTERN_REFERANSE_ID_REGEX = Regex("^[a-zA-Z0-9-._~!$&\"\\\\*+,;=:@]+$")
    }
}
