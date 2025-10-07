package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.jpa.defaultAttributeConverters
import org.hibernate.SessionFactory

fun createSessionFactory(
    configure: SessionFactoryConfiguration.() -> Unit = {},
): SessionFactory {
    val configuration = SessionFactoryConfiguration()
        .apply {
            defaultAttributeConverters(true)
            resourceLocal()
            snakeCase()
        }
        .apply(configure)
    return configuration.build()
}
