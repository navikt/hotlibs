package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.database.JdbcOperations
import no.nav.hjelpemidler.database.Row
import no.nav.hjelpemidler.database.Store
import no.nav.hjelpemidler.database.UpdateResult
import no.nav.hjelpemidler.database.sql.Sql
import no.nav.hjelpemidler.database.toQueryParameters

class TestStore(private val tx: JdbcOperations) : Store {
    fun lagre(entity: TestEntity): TestId =
        tx.single(
            sql = SQL_INSERT,
            queryParameters = entity.toQueryParameters(),
            mapper = Row::testId,
        )

    fun lagre(entities: List<TestEntity>): List<Long> =
        tx.batchAndReturnGeneratedKeys(
            sql = SQL_INSERT,
            items = entities.toList(),
            block = TestEntity::toQueryParameters,
        )

    fun hent(id: TestId): TestEntity =
        tx.single(
            sql = "SELECT * FROM test WHERE id = :id",
            queryParameters = id.toQueryParameters(),
            mapper = Row::toTestEntity,
        )

    fun hent(ids: Collection<Long>): List<TestEntity> =
        tx.list(
            sql = "SELECT * FROM test WHERE id = ANY (:ids)",
            queryParameters = mapOf("ids" to ids.toTypedArray()),
            mapper = Row::toTestEntity,
        )

    fun hentMap(id: TestId, vararg columnLabels: String): Map<String, Any?> =
        tx.single(
            sql = "SELECT ${columnLabels.joinToString()} FROM test WHERE id = :id",
            queryParameters = id.toQueryParameters(),
        ) { it.toMap() }

    fun hentTree(id: TestId): JsonNode =
        tx.single(
            sql = "SELECT * FROM test WHERE id = :id",
            queryParameters = id.toQueryParameters(),
        ) { it.toTree() }

    fun oppdater(id: TestId, integer: Int): UpdateResult {
        return tx.update(
            sql = "UPDATE test SET integer = :integer WHERE id = :id",
            queryParameters = mapOf("id" to id, "integer" to integer),
        )
    }

    fun slett(id: TestId): Boolean =
        tx.execute(
            sql = "DELETE FROM test WHERE id = :id",
            queryParameters = id.toQueryParameters(),
        )

    companion object {
        val SQL_INSERT = Sql(
            """
                INSERT INTO test (string, integer, enum, data_1, data_2, fnr, aktor_id, navn)
                VALUES (:string, :integer, :enum, :data_1, :data_2, :fnr, :aktor_id, (:fornavn, :mellomnavn, :etternavn)::gyldig_personnavn)
                RETURNING id
            """.trimIndent()
        )
    }
}
