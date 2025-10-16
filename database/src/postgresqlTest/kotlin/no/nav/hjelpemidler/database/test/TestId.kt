package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.domain.id.LongId

class TestId @JsonCreator constructor(value: Long = 0) : LongId(value) {
    @JsonCreator
    constructor(value: String) : this(value.toLong())
}

fun Row.testId(): TestId = TestId(long("id"))
