package no.nav.hjelpemidler.database

enum class H2Mode(internal vararg val parameters: Pair<String, String>) {
    ORACLE(
        "MODE" to "Oracle",
        "DEFAULT_NULL_ORDERING" to "HIGH",
    ),
    POSTGRESQL(
        "MODE" to "PostgreSQL",
        "DEFAULT_NULL_ORDERING" to "HIGH",
        "DATABASE_TO_LOWER" to "TRUE",
    ),
    REGULAR(
        "MODE" to "REGULAR",
    )
    ;
}
