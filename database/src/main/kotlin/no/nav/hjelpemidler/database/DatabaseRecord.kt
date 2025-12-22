package no.nav.hjelpemidler.database

interface DatabaseRecord {
    /**
     * Konverter hele raden til [Map].
     */
    fun asMap(): Map<String, Any?>
}
