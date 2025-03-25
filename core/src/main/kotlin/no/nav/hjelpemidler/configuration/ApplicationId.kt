package no.nav.hjelpemidler.configuration

/**
 * ID for å kunne vite hvilken applikasjon/løsning/system vi snakker om.
 *
 * @see [no.nav.hjelpemidler.domain.id.EksternId]
 */
interface ApplicationId {
    val application: String
}

data object HotsakApplicationId : ApplicationId {
    override val application: String = "hotsak"
}

data object BehovsmeldingApplicationId : ApplicationId {
    override val application: String = "behovsmelding"
}
