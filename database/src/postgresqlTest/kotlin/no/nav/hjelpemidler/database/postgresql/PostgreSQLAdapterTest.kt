package no.nav.hjelpemidler.database.postgresql

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.single
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.transactionAsync
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test

class PostgreSQLAdapterTest {
    @Test
    fun `Henter alle value types`() = runTest {
        transactionAsync(testDataSource) {
            it.single<BigDecimal>("SELECT 3.14::NUMERIC") shouldBe BigDecimal("3.14")
            it.single<BigInteger>("SELECT 1::BIGINT") shouldBe BigInteger.ONE
            it.single<Boolean>("SELECT TRUE") shouldBe true
            // it.single<Byte>("SELECT 1::SMALLINT") shouldBe 1.toByte()
            it.single<ByteArray>("SELECT 'test'::bytea") shouldBe "test".toByteArray()
            it.single<Double>("SELECT 3.14::float8") shouldBe 3.14
            it.single<Float>("SELECT 3.14::float4") shouldBe 3.14F
            it.single<Int>("SELECT 1::int4") shouldBe 1
            it.single<Long>("SELECT 1::int8") shouldBe 1L
            it.single<Short>("SELECT 1::int2") shouldBe 1
            it.single<String>("SELECT 'test'") shouldBe "test"
            it.single<UUID>("SELECT gen_random_uuid()").shouldBeInstanceOf<UUID>()

            it.single<Instant>("SELECT CURRENT_TIMESTAMP").shouldBeInstanceOf<Instant>()
            it.single<LocalDate>("SELECT CURRENT_DATE").shouldBeInstanceOf<LocalDate>()
            it.single<LocalDateTime>("SELECT LOCALTIMESTAMP").shouldBeInstanceOf<LocalDateTime>()
            it.single<LocalTime>("SELECT LOCALTIME").shouldBeInstanceOf<LocalTime>()
            it.single<OffsetDateTime>("SELECT CURRENT_TIMESTAMP").shouldBeInstanceOf<OffsetDateTime>()
            it.single<OffsetTime>("SELECT CURRENT_TIME").shouldBeInstanceOf<OffsetTime>()
            it.single<ZonedDateTime>("SELECT CURRENT_TIMESTAMP").shouldBeInstanceOf<ZonedDateTime>()
        }
    }
}
