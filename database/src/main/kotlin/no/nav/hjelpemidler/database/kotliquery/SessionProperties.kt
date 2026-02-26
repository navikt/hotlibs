package no.nav.hjelpemidler.database.kotliquery

internal data class SessionProperties(
    val readOnly: Boolean = false,
    val returnGeneratedKeys: Boolean = false,
    val strict: Boolean = true,
    val queryTimeout: Int? = null,
) {
    override fun toString(): String =
        "readOnly: $readOnly, returnGeneratedKeys: $returnGeneratedKeys, strict: $strict, queryTimeout: $queryTimeout"
}
