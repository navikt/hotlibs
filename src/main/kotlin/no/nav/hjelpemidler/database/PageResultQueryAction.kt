package no.nav.hjelpemidler.database

import kotliquery.Query
import kotliquery.Row
import kotliquery.Session
import kotliquery.action.QueryAction
import kotlin.random.Random

data class PageResultQueryAction<A>(
    val query: Query,
    val extractor: (Row) -> A?,
    val limit: Int,
    val offset: Int,
    val totalNumberOfItemsLabel: String = "total",
) : QueryAction<Page<A>> {
    override fun runWithSession(session: Session): Page<A> {
        var totalNumberOfItems = -1
        val suffix = Random.nextInt(1000, 10000)
        val limitLabel = "limit_$suffix"
        val offsetLabel = "offset_$suffix"
        val items = session.list(
            query.copy(
                statement = "${query.statement}\nLIMIT :$limitLabel OFFSET :$offsetLabel",
                paramMap = query.paramMap.plus(
                    mapOf(
                        limitLabel to limit + 1, // fetch one more than limit to check for "hasMore"
                        offsetLabel to offset,
                    )
                )
            )
        ) { row ->
            totalNumberOfItems = row.intOrNull(totalNumberOfItemsLabel) ?: -1
            extractor(row)
        }
        return Page(
            items = items.take(limit),
            total = totalNumberOfItems,
        )
    }
}
