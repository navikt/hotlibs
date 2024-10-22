package no.nav.hjelpemidler.database.test

import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.nulls.shouldNotBeNull
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.domain.id.Id

class TestId(value: Long = 0) : Id<Long>(value)

fun TestId?.shouldBeValid() {
    this.shouldNotBeNull()
    this.value.shouldBePositive()
}

fun Row.testId(): TestId = TestId(long("id"))
