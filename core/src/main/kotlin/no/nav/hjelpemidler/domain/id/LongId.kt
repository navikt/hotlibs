package no.nav.hjelpemidler.domain.id

import java.sql.Types

abstract class LongId(value: Long) : Id<Long>(value) {
    companion object {
        const val SQL_TYPE: Int = Types.BIGINT
    }
}
