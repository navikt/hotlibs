package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPayloadHolder
import no.nav.hjelpemidler.domain.person.Fødselsnummer

/**
 * @see <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_access_token#by-value--self-contained-access-token">access token from ID-porten | ID-porten</a>
 */
class TokenXCredential(credential: JWTCredential) : JWTPayloadHolder(credential.payload) {
    val fnr: Fødselsnummer? get() = get(PID_CLAIM)?.let(::Fødselsnummer)
    val level: SecurityLevelOfAssurance? get() = get(ACR_CLAIM)?.let(SecurityLevelOfAssurance::of)

    companion object {
        /**
         * Sikkerhetsnivå.
         */
        const val ACR_CLAIM = "acr"

        /**
         * Fødselsnummer/D-nummer.
         */
        const val PID_CLAIM = "pid"
    }
}

/**
 * <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_id_token#acr-values">id_token from ID-porten | ID-porten</a>
 */
enum class SecurityLevelOfAssurance(private val value: String) {
    /** Tidligere `Level3` */
    SUBSTANTIAL(value = "idporten-loa-substantial"),

    /** Tidligere `Level4` */
    HIGH(value = "idporten-loa-high"),
    ;

    override fun toString(): String = value

    companion object {
        fun of(value: String): SecurityLevelOfAssurance? = entries.firstOrNull { it.value == value }
    }
}
