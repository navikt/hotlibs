package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import java.util.UUID

/**
 * @see <a href="https://docs.nais.io/auth/entra-id/reference/#claims">Entra ID reference - Nais</a>
 */
sealed class EntraIdCredential(credential: JWTCredential) : TexasCredential(credential) {
    class Application(credential: JWTCredential) : EntraIdCredential(credential) {
        val id: Applikasjonsnavn get() = Applikasjonsnavn(mustGet(AZP_NAME_CLAIM).split(":").last())
    }

    class User(credential: JWTCredential) : EntraIdCredential(credential) {
        val id: NavIdent get() = mustGet(NAV_IDENT_CLAIM).let(::NavIdent)
        val navn: String get() = mustGet(NAME_CLAIM)
        val epost: String get() = mustGet(PREFERRED_USERNAME_CLAIM)
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
             */
            const val GROUPS_CLAIM = "groups"
        }
    }

    companion object {
        /** Authorized party. */
        const val AZP_CLAIM = "azp"

        /** Authorized party name. */
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
                Application(credential)
            } else {
                User(credential)
            }
    }
}
