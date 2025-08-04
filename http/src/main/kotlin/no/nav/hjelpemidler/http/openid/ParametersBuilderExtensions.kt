package no.nav.hjelpemidler.http.openid

import io.ktor.http.ParametersBuilder

fun ParametersBuilder.assertion(value: String) =
    append("assertion", value)

fun ParametersBuilder.clientId(value: String) =
    append("client_id", value)

fun ParametersBuilder.clientSecret(value: String) =
    append("client_secret", value)

fun ParametersBuilder.grantType(value: String) =
    append("grant_type", value)

fun ParametersBuilder.requestedTokenUse(value: String) =
    append("requested_token_use", value)

fun ParametersBuilder.scope(value: String) =
    append("scope", value)
