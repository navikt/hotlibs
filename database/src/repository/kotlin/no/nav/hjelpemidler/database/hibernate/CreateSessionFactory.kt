package no.nav.hjelpemidler.database.hibernate

import org.hibernate.SessionFactory

fun createSessionFactory(
    configure: SessionFactoryConfiguration.() -> Unit = {},
): SessionFactory {
    val configuration = SessionFactoryConfiguration()
        .apply {
            resourceLocal()
            snakeCase()
        }
        .apply(configure)
    return configuration.build()
}
