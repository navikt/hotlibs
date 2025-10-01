package no.nav.hjelpemidler.database.test

import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.nulls.shouldNotBeNull
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.domain.id.LongId

class TestId(value: Long = 0) : LongId(value)

fun TestId?.shouldBeValid() {
    this.shouldNotBeNull()
    this.value.shouldBePositive()
}

fun Row.testId(): TestId = TestId(long("id"))
