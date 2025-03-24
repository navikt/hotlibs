package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.hjelpemidler.collections.toQueryString
import no.nav.hjelpemidler.net.parameters
import java.net.URI

/**
 * ID-implementasjon basert på [URI].
 *
 * Gjør det lettere å kombinere ulike informasjonselementer i én string.
 *
 * Kan f.eks. benyttes som `eksternReferanseId` mot Joark.
 */
class EksternId private constructor(private val uri: URI) {
    @JsonCreator
    constructor(uri: String) : this(URI(uri))

    constructor(
        application: String,
        resource: String,
        parameters: Map<String, List<String>>,
    ) : this("$SCHEME://$application/$resource${parameters.toQueryString()}")

    constructor(
        application: String,
        resource: String,
        vararg parameters: Pair<String, String>,
    ) : this(application, resource, parameters.groupBy({ (key, _) -> key }, { (_, value) -> value }))

    val application: String get() = uri.host
    val resource: String get() = uri.path
    val parameters: Map<String, List<String>> get() = uri.parameters

    operator fun get(key: String): String? = parameters[key]?.firstOrNull()

    fun toURI(): URI = uri

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EksternId
        return uri == other.uri
    }

    override fun hashCode(): Int = uri.hashCode()

    @JsonValue
    override fun toString(): String = uri.toString()

    companion object {
        const val SCHEME = "app"
    }
}
