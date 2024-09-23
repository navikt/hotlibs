package no.nav.hjelpemidler.domain.person

import org.intellij.lang.annotations.Language

val fnr = Fødselsnummer(30.år)
val fnrJson = """"$fnr""""

data class Person1(val fnr: Fødselsnummer)

data class Person2(val fnr: Fødselsnummer?)

@Language("JSON")
val person1Json = """{"fnr":"$fnr"}"""

@Language("JSON")
val person2Json = """{"fnr":null}"""

@Language("JSON")
val fnrAsArrayElementJson = """["$fnr"]"""

@Language("JSON")
val fnrAsMapKeyJson = """{"$fnr":true}"""
