package no.nav.hjelpemidler.domain.id

import java.util.UUID

class TestLongId(value: Long) : LongId(value) {
    constructor(value: String) : this(value.toLong())
}

class TestStringId(value: String) : StringId(value) {
    constructor(value: Long) : this(value.toString())
}

class TestUuidId(value: UUID) : Id<UUID>(value) {
    constructor(value: String) : this(UUID.fromString(value))
}
