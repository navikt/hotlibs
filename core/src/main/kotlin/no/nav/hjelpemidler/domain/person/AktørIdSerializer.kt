package no.nav.hjelpemidler.domain.person

internal object AktørIdSerializer : PersonIdentSerializer<AktørId>() {
    override fun deserialize(value: String): AktørId = AktørId(value)
}
