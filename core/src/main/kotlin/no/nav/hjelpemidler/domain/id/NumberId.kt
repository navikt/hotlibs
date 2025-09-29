package no.nav.hjelpemidler.domain.id

import java.sql.Types

abstract class NumberId(value: Long) : Id<Long>(value) {
    companion object {
        val SQL_TYPE: Int = Types.BIGINT
    }
}
