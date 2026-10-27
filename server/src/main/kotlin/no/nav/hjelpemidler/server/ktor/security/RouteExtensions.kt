package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route

fun Route.authenticateEntraId(
    strategy: AuthenticationStrategy = AuthenticationStrategy.Required,
    build: Route.() -> Unit,
): Route = authenticate(AuthenticationConfiguration.ENTRA_ID, strategy = strategy, build = build)

fun Route.authenticateTokenX(
    strategy: AuthenticationStrategy = AuthenticationStrategy.Required,
    build: Route.() -> Unit,
): Route = authenticate(AuthenticationConfiguration.TOKEN_X, strategy = strategy, build = build)
