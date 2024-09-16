package no.nav.hjelpemidler.domain.geografi

data class Veiadresse(
    val adresse: String,
    val postnummer: String,
    val poststed: String,
) : GeografiskTilknytning {
    override fun toString(): String = "$adresse, $postnummer $poststed"
}

fun lagVeiadresse(adresse: String, postnummer: String, poststed: String): Veiadresse =
    Veiadresse(adresse.trim(), postnummer.trim(), poststed.trim())

@JvmName("lagVeiadresseOrNull")
fun lagVeiadresse(adresse: String?, postnummer: String?, poststed: String?): Veiadresse? =
    if (adresse.isNullOrBlank() || postnummer.isNullOrBlank() || poststed.isNullOrBlank()) {
        null
    } else {
        lagVeiadresse(adresse, postnummer, poststed)
    }
