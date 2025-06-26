package no.nav.hjelpemidler.database.hibernate.test

import no.nav.hjelpemidler.database.hibernate.createSessionFactory

val testSessionFactory by lazy {
    createSessionFactory {
        annotatedClass<TestSakEntity>()
        annotatedClass<TestSaksstatus>()
        dataSource(testDataSource)
        showSql(showSql = false, formatSql = false, highlightSql = false)
    }
}
