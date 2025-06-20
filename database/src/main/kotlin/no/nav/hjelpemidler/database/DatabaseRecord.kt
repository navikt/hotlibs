package no.nav.hjelpemidler.database

interface DatabaseRecord {
    fun toMap(): Map<String, Any?>
}
