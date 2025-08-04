package no.nav.hjelpemidler.http.openid

import no.nav.hjelpemidler.configuration.Environment

fun scopeOf(
    application: String,
    namespace: String = "teamdigihot",
    cluster: Environment = Environment.current,
): String = "api://$cluster.$namespace.$application/.default"
