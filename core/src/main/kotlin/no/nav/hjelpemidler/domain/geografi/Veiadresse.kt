package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
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
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        fun fromJson(
            @JsonProperty("adresselinje1") adresselinje1: String? = null,
            @JsonProperty("adresselinje2") adresselinje2: String? = null,
            @JsonProperty("adresselinje3") adresselinje3: String? = null,
            @JsonProperty("adresse") legacyAdresse: String? = null,
            @JsonProperty("postnummer") postnummer: String,
            @JsonProperty("poststed") poststed: String,
        ): Veiadresse {
            val splitAddress = (if (!adresselinje1.isNullOrBlank()) {
                listOf(adresselinje1)
            } else {
                legacyAdresse
                    ?.split(",")
                    ?.map { it.trim() }
                    ?.filter { it.isNotEmpty() }
                    .orEmpty()
            }).filter { it.isNotBlank() }

            val resolvedAddressLine1 = when {
                !adresselinje1.isNullOrBlank() -> adresselinje1.trim()
                splitAddress.isNotEmpty() -> splitAddress[0]
                else -> error("Mangler adresselinje1")
            }
            val resolvedAddressLine2 = when {
                !adresselinje2.isNullOrBlank() -> adresselinje2.trim()
                splitAddress.size > 1 -> splitAddress[1]
                else -> null
            }
            val resolvedAddressLine3 = when {
                !adresselinje3.isNullOrBlank() -> adresselinje3.trim()
                splitAddress.size > 2 -> splitAddress[2]
                else -> null
            }

            return Veiadresse(
                adresselinje1 = resolvedAddressLine1,
                adresselinje2 = resolvedAddressLine2,
                adresselinje3 = resolvedAddressLine3,
                postnummer = postnummer.trim(),
                poststed = poststed.trim(),
            )
        }

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
