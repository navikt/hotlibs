package no.nav.hjelpemidler.http.openid

import io.ktor.http.ParametersBuilder

internal fun ParametersBuilder.assertion(value: String) =
    append("assertion", value)

internal fun ParametersBuilder.clientId(value: String) =
    append("client_id", value)

internal fun ParametersBuilder.clientSecret(value: String) =
    append("client_secret", value)

internal fun ParametersBuilder.grantType(value: String) =
    append("grant_type", value)

internal fun ParametersBuilder.requestedTokenUse(value: String) =
    append("requested_token_use", value)

internal fun ParametersBuilder.scope(value: String) =
    append("scope", value)
