package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.Oracle
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.transaction
import javax.sql.DataSource

val testDataSource: DataSource by lazy {
    createDataSource(Oracle) {
        jdbcUrl = "jdbc:oracle:thin:@//localhost:1521/freepdb1"
        username = "test"
        password = "test"
        databaseName = "test"
    }.also {
        transaction(it) { tx ->
            tx.execute(
                //language=Oracle
                "DROP TABLE IF EXISTS test"
            )
            tx.execute(
                //language=Oracle
                """
                    CREATE TABLE test
                    (
                        id                      NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        "INTEGER"               NUMBER                      DEFAULT 10,
                        "LONG"                  NUMBER(19)                  DEFAULT 20,
                        string                  VARCHAR2(255)               DEFAULT 'string',
                        enum                    VARCHAR2(255)               DEFAULT 'A',
                        data                    CLOB                        DEFAULT '{}',
                        fnr                     CHAR(11),
                        aktor_id                CHAR(13)                    DEFAULT '1234567891011',
                        boolean                 boolean,
                        "DATE"                  DATE                        DEFAULT CURRENT_DATE,
                        timestamp               TIMESTAMP(6)                DEFAULT CURRENT_TIMESTAMP,
                        timestamp_with_timezone TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        uuid                    RAW(16)
                    )
                """.trimIndent()
            )
        }
    }
}
