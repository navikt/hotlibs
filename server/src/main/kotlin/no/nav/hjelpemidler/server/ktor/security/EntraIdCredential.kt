package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPayloadHolder
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import java.util.UUID

/**
 * @see <a href="https://docs.nais.io/auth/entra-id/reference/#claims">Entra ID reference - Nais</a>
 */
sealed class EntraIdCredential(credential: JWTCredential) : JWTPayloadHolder(credential.payload) {
    fun getClaim(name: String): String = checkNotNull(this[name]) { "'$name' claim mangler" }

    class Applikasjon(credential: JWTCredential) : EntraIdCredential(credential) {
        val id: Applikasjonsnavn get() = Applikasjonsnavn(getClaim(AZP_NAME_CLAIM).split(":").last())
    }

    class Ansatt(credential: JWTCredential) : EntraIdCredential(credential) {
        val id: NavIdent get() = getClaim(NAV_IDENT_CLAIM).let(::NavIdent)
        val navn: String get() = getClaim(NAME_CLAIM)
        val epost: String get() = getClaim(PREFERRED_USERNAME_CLAIM)
        val grupper: Set<UUID> get() = getListClaim(GROUPS_CLAIM, UUID::class).toSet()

        companion object {
            /**
             * @see [NavIdent]
             */
            const val NAV_IDENT_CLAIM = "NAVident"

            /**
             * Den ansattes navn.
             */
            const val NAME_CLAIM = "name"

            /**
             * Den ansattes e-post.
             */
            const val PREFERRED_USERNAME_CLAIM = "preferred_username"

            /**
             * Den ansattes grupper.
             *
             * @see [EntraIdGruppe]
             */
            const val GROUPS_CLAIM = "groups"
        }
    }

    companion object {
        /** authorized party */
        const val AZP_CLAIM = "azp"

        /** authorized party name */
        const val AZP_NAME_CLAIM = "azp_name"

        /**
         * Har verdien [TOKEN_TYPE_APP] hvis systembruker (maskin til maskin).
         */
        const val TOKEN_TYPE_CLAIM = "idtyp"
        const val TOKEN_TYPE_APP = "app"

        /**
         * Opprett [EntraIdCredential] basert på [TOKEN_TYPE_CLAIM].
         */
        operator fun invoke(credential: JWTCredential): EntraIdCredential =
            if (credential[TOKEN_TYPE_CLAIM] == TOKEN_TYPE_APP) {
                Applikasjon(credential)
            } else {
                Ansatt(credential)
            }
    }
}
