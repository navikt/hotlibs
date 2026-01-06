package no.nav.hjelpemidler.database

import io.mockk.mockk
import io.mockk.verifySequence
import no.nav.hjelpemidler.database.generic.GenericRow
import no.nav.hjelpemidler.test.NamedTestCase
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import java.sql.ResultSet
import java.util.UUID

class RowTest {
    private val uuid: UUID = UUID.randomUUID()

    @TestFactory
    fun sqlTypes() = sequenceOf(
        Case(Row::any, ResultSet::getObject),
        Case(Row::anyOrNull, ResultSet::getObject),
        Case(Row::asciiStream, ResultSet::getAsciiStream),
        Case(Row::asciiStreamOrNull, ResultSet::getAsciiStream),
        Case(Row::bigDecimal, ResultSet::getBigDecimal),
        Case(Row::bigDecimalOrNull, ResultSet::getBigDecimal),
        Case(Row::binaryStream, ResultSet::getBinaryStream),
        Case(Row::binaryStreamOrNull, ResultSet::getBinaryStream),
        Case(Row::blob, ResultSet::getBlob),
        Case(Row::blobOrNull, ResultSet::getBlob),
        Case(Row::boolean, ResultSet::getBoolean),
        Case(Row::booleanOrNull, ResultSet::getBoolean),
        Case(Row::byte, ResultSet::getByte),
        Case(Row::byteOrNull, ResultSet::getByte),
        Case(Row::bytes, ResultSet::getBytes),
        Case(Row::bytesOrNull, ResultSet::getBytes),
        Case(Row::clob, ResultSet::getClob),
        Case(Row::clobOrNull, ResultSet::getClob),
        Case(Row::double, ResultSet::getDouble),
        Case(Row::doubleOrNull, ResultSet::getDouble),
        Case(Row::float, ResultSet::getFloat),
        Case(Row::floatOrNull, ResultSet::getFloat),
        Case(Row::int, ResultSet::getInt),
        Case(Row::intOrNull, ResultSet::getInt),
        Case(Row::long, ResultSet::getLong),
        Case(Row::longOrNull, ResultSet::getLong),
        Case(Row::short, ResultSet::getShort),
        Case(Row::shortOrNull, ResultSet::getShort),
        Case(Row::sqlArray, ResultSet::getArray),
        Case(Row::sqlArrayOrNull, ResultSet::getArray),
        Case(Row::string, ResultSet::getString),
        Case(Row::stringOrNull, ResultSet::getString),
    ).testFactory()

    class Case(
        val rowFn: Row.(Int) -> Any?,
        val resultSetFn: ResultSet.(Int) -> Any?,
    ) : NamedTestCase<Case> {
        override fun getName(): String {
            val a = rowFn.toString()
                .removePrefix("fun no.nav.hjelpemidler.database.")
                .substringBefore('(')
            val b = resultSetFn.toString()
                .removePrefix("fun java.sql.")
                .substringBefore('(')
            return "$a calls $b"
        }

        override fun getPayload(): Case = this
        override fun invoke() {
            val columnIndex = 1
            val resultSet = mockk<ResultSet>(relaxed = true)
            val row = GenericRow(resultSet)
            row.rowFn(columnIndex)
            verifySequence {
                resultSet.resultSetFn(columnIndex)
                resultSet.wasNull()
            }
        }
    }
}
