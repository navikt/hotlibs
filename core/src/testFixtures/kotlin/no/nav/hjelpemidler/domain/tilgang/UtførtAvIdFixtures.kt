package no.nav.hjelpemidler.domain.tilgang

private val navIdentSuffixRange: IntRange = 100000..999999
fun lagTilfeldigNavIdent(): NavIdent =
    NavIdent("""${NavIdent.FIRST_CHARACTER_RANGE.random()}${navIdentSuffixRange.random()}""")
