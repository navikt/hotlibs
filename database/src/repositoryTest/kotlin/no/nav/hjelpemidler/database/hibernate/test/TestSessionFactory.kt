package no.nav.hjelpemidler.database.hibernate.test

import no.nav.hjelpemidler.database.hibernate.createSessionFactory

val testSessionFactory by lazy {
    createSessionFactory {
        managedClass<TestSakEntity>()
        managedClass<TestSaksstatusEntity>()
        dataSource(testDataSource)
        showSql(showSql = false, formatSql = false, highlightSql = false)
    }
}
