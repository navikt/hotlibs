package no.nav.hjelpemidler.domain.person

internal object FødselsnummerSerializer : PersonIdentSerializer<Fødselsnummer>() {
    override fun deserialize(value: String): Fødselsnummer = Fødselsnummer(value)
}
