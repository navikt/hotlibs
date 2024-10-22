package no.nav.hjelpemidler.validering

fun nummerValidator(lengde: Int): Validator<String> {
    val regex: Regex = Regex("^[0-9]{$lengde}$")
    return Validator { value -> value matches regex }
}
