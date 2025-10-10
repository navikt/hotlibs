package no.nav.hjelpemidler.database

interface DatabaseRecord {
    /**
     * Transformer hele raden til `Map<String, Any?>`.
     */
    fun toMap(): Map<String, Any?>
}
