package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.interfaces.Verification
import io.ktor.server.auth.jwt.JWTCredential
import no.nav.hjelpemidler.domain.person.Fødselsnummer

/**
 * @see <a href="https://docs.nais.io/auth/tokenx/reference/#claims">TokenX reference - Nais</a>
 * @see <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_access_token#by-value--self-contained-access-token">access token from ID-porten | ID-porten</a>
 */
class TokenXCredential(credential: JWTCredential) : TexasCredential(credential) {
    val fnr: Fødselsnummer get() = mustGet(PID_CLAIM).let(::Fødselsnummer)
    val level: IdPortenSecurityLevel get() = mustGet(ACR_CLAIM).let(IdPortenSecurityLevel::of)

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
 * @see <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_id_token#acr-values">id_token from ID-porten | ID-porten</a>
 */
enum class IdPortenSecurityLevel(internal val value: String, internal val relativeValue: Int) {
    /** Ikke i bruk pt. */
    LOW(value = "idporten-loa-low", relativeValue = 1),

    /** Tidligere `Level3` */
    SUBSTANTIAL(value = "idporten-loa-substantial", relativeValue = 2),

    /** Tidligere `Level4` */
    HIGH(value = "idporten-loa-high", relativeValue = 3),
    ;

    override fun toString(): String = value

    companion object {
        internal const val LEVEL_3 = "Level3"
        internal const val LEVEL_4 = "Level4"

        fun of(value: String): IdPortenSecurityLevel = when (value) {
            LEVEL_3 -> SUBSTANTIAL
            LEVEL_4 -> HIGH
            else -> entries.firstOrNull { it.value == value } ?: error("Ukjent acr-claim: '$value'")
        }
    }
}

fun Verification.withMinimumSecurityLevel(minimumLevel: IdPortenSecurityLevel): Verification =
    withClaim(TokenXCredential.ACR_CLAIM) { claim, _ ->
        runCatching {
            val relativeValue = IdPortenSecurityLevel.of(claim.asString()).relativeValue
            relativeValue >= minimumLevel.relativeValue
        }.getOrDefault(false)
    }
