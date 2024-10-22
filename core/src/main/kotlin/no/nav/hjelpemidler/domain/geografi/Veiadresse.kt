package no.nav.hjelpemidler.domain.geografi

import no.nav.hjelpemidler.validering.nummerValidator

data class Veiadresse(
    val adresse: String,
    val postnummer: String,
    val poststed: String,
) : GeografiskTilknytning {
    init {
        require(adresse.isNotBlank()) { "Ugyldig adresse: '$adresse'" }
        require(postnummerValidator(postnummer)) { "Ugyldig postnummer: '$postnummer'" }
        require(poststed.isNotBlank()) { "Ugyldig poststed: '$poststed'" }
    }

    override fun toString(): String = "$adresse, $postnummer $poststed"

    companion object {
        private val postnummerValidator = nummerValidator(lengde = 4)
    }
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
