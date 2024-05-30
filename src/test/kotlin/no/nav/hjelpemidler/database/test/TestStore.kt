package no.nav.hjelpemidler.database.test

import kotliquery.Row
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.Store
import no.nav.hjelpemidler.database.toMap
import no.nav.hjelpemidler.database.toQueryParameters
import org.intellij.lang.annotations.Language

class TestStore(private val tx: JdbcOperations) : Store {
    @Language("PostgreSQL")
    private val insertSql = """
        INSERT INTO test (string, integer, enum, data_1, data_2)
        VALUES (:string, :integer, :enum, :data_1, :data_2)
        RETURNING id
    """.trimIndent()

    fun lagre(entity: TestEntity): TestId =
        tx.single(
            sql = insertSql,
            queryParameters = entity.toQueryParameters()
        ) { row ->
            row.long("id").let(::TestId)
        }

    fun lagre(entities: List<TestEntity>): List<Long> =
        tx.batchAndReturnGeneratedKeys(
            sql = insertSql,
            items = entities.toList(),
            block = TestEntity::toQueryParameters,
        )

    fun hent(id: TestId): Map<String, Any?> =
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
