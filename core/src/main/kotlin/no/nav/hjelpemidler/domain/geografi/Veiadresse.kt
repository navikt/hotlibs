package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.text.isInteger

data class Veiadresse(
    @JsonAlias("adresse")
    val adresselinje1: String,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val postnummer: String,
    val poststed: String,
) {
    init {
        require(adresselinje1.isNotBlank()) { "Ugyldig adresselinje 1: '$adresselinje1'" }
        require(erGyldigPostnummer(postnummer)) { "Ugyldig postnummer: '$postnummer'" }
        require(poststed.isNotBlank()) { "Ugyldig poststed: '$poststed'" }
    }

    val adresse: String
        get() = listOfNotNull(adresselinje1, adresselinje2, adresselinje3)
            .joinToString(", ")

    override fun toString(): String = "$adresse, $postnummer $poststed"

    companion object {
        fun erGyldigPostnummer(value: String): Boolean = value.length == 4 && value.isInteger()
    }
}

fun lagVeiadresse(adresselinje1: String, adresselinje2: String? = null, adresselinje3: String? = null, postnummer: String, poststed: String): Veiadresse =
    Veiadresse(adresselinje1.trim(), adresselinje2?.trim(), adresselinje3?.trim(), postnummer.trim(), poststed.trim())

@JvmName("lagVeiadresseOrNull")
fun lagVeiadresse(adresselinje1: String?, adresselinje2: String?, adresselinje3: String?, postnummer: String?, poststed: String?): Veiadresse? =
    if (adresselinje1.isNullOrBlank() || postnummer.isNullOrBlank() || poststed.isNullOrBlank()) {
        null
    } else {
        lagVeiadresse(adresselinje1, adresselinje2, adresselinje3, postnummer, poststed)
    }
