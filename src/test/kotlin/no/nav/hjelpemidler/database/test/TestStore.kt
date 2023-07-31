package no.nav.hjelpemidler.database.test

import kotliquery.Row
import kotliquery.Session
import no.nav.hjelpemidler.database.Store
import no.nav.hjelpemidler.database.batchAndReturnGeneratedKeys
import no.nav.hjelpemidler.database.single
import no.nav.hjelpemidler.database.toMap
import no.nav.hjelpemidler.database.toQueryParameters
import org.intellij.lang.annotations.Language

class TestStore(private val tx: Session) : Store {
    @Language("PostgreSQL")
    private val insertSql = """
        INSERT INTO test (string, integer, enum, data_1, data_2)
        VALUES (:string, :integer, :enum, :data_1, :data_2)
        RETURNING id
    """.trimIndent()

    fun lagre(entity: TestEntity): Long =
        tx.single(
            sql = insertSql,
            queryParameters = entity.toQueryParameters()
        ) { row ->
            row.long("id")
        }

    fun lagre(entities: List<TestEntity>): List<Long> =
        tx.batchAndReturnGeneratedKeys(
            sql = insertSql,
            items = entities.toList(),
            block = TestEntity::toQueryParameters,
        )

    fun hent(id: Long): Map<String, Any?> =
        tx.single(
            sql = """
                SELECT *
                FROM test
                WHERE id = :id
            """.trimIndent(),
            queryParameters = id.toQueryParameters(),
            mapper = Row::toMap,
        )
}
