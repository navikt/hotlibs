package no.nav.hjelpemidler.http.openid

import no.nav.hjelpemidler.configuration.Environment

fun scopeOf(application: String, namespace: String = "teamdigihot"): String {
    val cluster = Environment.current
    return "api://$cluster.$namespace.$application/.default"
}
