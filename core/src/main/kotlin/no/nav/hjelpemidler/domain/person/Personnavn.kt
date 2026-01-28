package no.nav.hjelpemidler.domain.person

data class Personnavn(
    val fornavn: String,
    val mellomnavn: String? = null,
    val etternavn: String,
) {
    override fun toString(): String = listOfNotNull(fornavn, mellomnavn, etternavn)
        .filter(String::isNotBlank)
        .joinToString(" ", transform = String::trim)

    companion object {
        val UKJENT = Personnavn(fornavn = "Ukjent", etternavn = "Ukjent")
    }
}

fun lagPersonnavn(fornavn: String, mellomnavn: String? = null, etternavn: String): Personnavn =
    Personnavn(fornavn.trim(), mellomnavn?.trim(), etternavn.trim())

@JvmName("lagPersonnavnOrNull")
fun lagPersonnavn(fornavn: String?, mellomnavn: String? = null, etternavn: String?): Personnavn? =
    if (fornavn.isNullOrBlank() || etternavn.isNullOrBlank()) {
        null
    } else {
        lagPersonnavn(fornavn, mellomnavn, etternavn)
    }

interface HarPersonnavn {
    val navn: Personnavn
}
