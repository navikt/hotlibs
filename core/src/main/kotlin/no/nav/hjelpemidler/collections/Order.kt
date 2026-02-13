package no.nav.hjelpemidler.collections

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import no.nav.hjelpemidler.collections.OrderBy.Direction

interface OrderBy<T> {
    val property: T
    val direction: Direction

    enum class Direction {
        @JsonAlias("asc", "ASC")
        ASCENDING,

        @JsonAlias("asc_nulls_first", "ASC_NULLS_FIRST")
        ASCENDING_NULLS_FIRST,

        @JsonAlias("desc", "DESC")
        DESCENDING,

        @JsonAlias("desc_nulls_last", "DESC_NULLS_LAST")
        DESCENDING_NULLS_LAST,

        @JsonEnumDefaultValue
        NONE,
        ;
    }
}

data class StringOrderBy(
    override val property: String,
    override val direction: Direction,
) : OrderBy<String>
