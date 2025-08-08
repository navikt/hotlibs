package no.nav.hjelpemidler.http.openid

import no.nav.hjelpemidler.configuration.Environment

@JvmInline
value class Target(private val value: String) {
    constructor(
        application: String,
        namespace: String = "teamdigihot",
        cluster: Environment = Environment.current,
    ) : this("api://$cluster.$namespace.$application/.default")

    override fun toString(): String = value
}
