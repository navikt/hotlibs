package no.nav.hjelpemidler.database

enum class Vendor(val databaseProductName: String) {
    H2("H2"),
    ORACLE("Oracle"),
    POSTGRESQL("PostgreSQL"),
    UNKNOWN(""),
}
