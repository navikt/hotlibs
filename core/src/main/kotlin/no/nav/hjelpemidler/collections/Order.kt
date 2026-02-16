package no.nav.hjelpemidler.collections

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import no.nav.hjelpemidler.collections.OrderBy.Direction

interface OrderBy<T> {
    val property: T
    val direction: Direction

    enum class Direction {
        /**
         * `NULLS LAST` is the default.
         */
        @JsonAlias("ASC")
        ASCENDING,

        @JsonAlias("ASC_NULLS_FIRST")
        ASCENDING_NULLS_FIRST,

        /**
         * `NULLS FIRST` is the default.
         */
        @JsonAlias("DESC")
        DESCENDING,

        @JsonAlias("DESC_NULLS_LAST")
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
