package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
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
    }
}
