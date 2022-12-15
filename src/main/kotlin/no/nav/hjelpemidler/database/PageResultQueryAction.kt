package no.nav.hjelpemidler.database

import kotliquery.Query
import kotliquery.Row
import kotliquery.Session
import kotliquery.action.QueryAction

data class PageResultQueryAction<A>(
    val query: Query,
    val extractor: (Row) -> A?,
    val limit: Int,
    val offset: Int,
    val totalNumberOfItemsLabel: String = "total",
) : QueryAction<Page<A>> {
    override fun runWithSession(session: Session): Page<A> {
        var totalNumberOfItems = -1
        val items = session.list(
            query.let {
                Query(
                    "${it.statement} limit :limit offset :offset",
                    it.params,
                    it.paramMap.plus(
                        mapOf(
                            "limit" to limit + 1, // fetch one more than limit to check for "hasMore"
                            "offset" to offset,
                        )
                    )
                )
            }
        ) {
            totalNumberOfItems = it.intOrNull(totalNumberOfItemsLabel) ?: -1
            extractor(it)
        }
        return Page(
            items = items.take(limit),
            total = totalNumberOfItems,
        )
    }
}
