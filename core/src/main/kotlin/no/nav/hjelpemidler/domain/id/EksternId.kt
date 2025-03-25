package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.hjelpemidler.configuration.ApplicationId
import java.util.UUID

/**
 * ID-implementasjon basert på [URN].
 *
 * Gjør det lettere å kombinere ulike informasjonselementer i én string.
 *
 * Kan f.eks. benyttes som `eksternReferanseId` mot Joark.
 */
class EksternId private constructor(private val urn: URN) {
    @JsonCreator
    constructor(urn: String) : this(URN(urn))

    constructor(
        application: String,
        resource: String,
        id: String,
    ) : this(URN(application, resource, id))

    constructor(
        application: String,
        resource: String,
        id: Number,
    ) : this(application, resource, id.toString())

    constructor(
        application: String,
        resource: String,
        id: UUID,
    ) : this(application, resource, id.toString())

    constructor(
        application: String,
        resource: String,
        id: Id<*>,
    ) : this(application, resource, id.toString())

    constructor(
        applicationId: ApplicationId,
        resource: String,
        id: String,
    ) : this(applicationId.application, resource, id)

    constructor(
        applicationId: ApplicationId,
        resource: String,
        id: Number,
    ) : this(applicationId.application, resource, id)

    constructor(
        applicationId: ApplicationId,
        resource: String,
        id: UUID,
    ) : this(applicationId.application, resource, id)

    constructor(
        applicationId: ApplicationId,
        resource: String,
        id: Id<*>,
    ) : this(applicationId.application, resource, id)

    init {
        val value = toString()
        require(value.matches(regex)) {
            "Ugyldig id: '$value'"
        }
    }

    val application: String get() = urn.nid
    val resource: String get() = urn.nss.substringBeforeLast(':')
    val id: String get() = urn.nss.substringAfterLast(':')

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EksternId
        return urn == other.urn
    }

    override fun hashCode(): Int = urn.hashCode()

    @JsonValue
    override fun toString(): String = urn.toString()

    fun toURN(): URN = urn

    companion object {
        /**
         * Hentet herfra: [CommonValidator.java](https://github.com/navikt/dokarkiv/blob/master/journalpost/src/main/java/no/nav/dokarkiv/journalpost/v1/validators/CommonValidator.java)
         */
        private val regex = Regex("^[a-zA-Z0-9-._~!$&\"\\\\*+,;=:@]+$")
    }
}
