package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.interfaces.Verification

inline fun <reified T> Verification.withAnyOfClaim(name: String, items: Collection<T>): Verification =
    withClaim(name) { claim, _ ->
        runCatching { claim.asList(T::class.java).any { it in items } }.getOrDefault(false)
    }
