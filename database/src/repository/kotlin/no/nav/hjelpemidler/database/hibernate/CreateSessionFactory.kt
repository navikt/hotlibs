package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.jpa.defaultAttributeConverters
import no.nav.hjelpemidler.database.repository.RepositoryConfiguration
import org.hibernate.SessionFactory

fun createSessionFactory(
    name: String = "default",
    configure: RepositoryConfiguration.() -> Unit = {},
): SessionFactory {
    val configuration = RepositoryConfiguration(name)
        .apply {
            defaultAttributeConverters()
            resourceLocal()
            snakeCase()
        }
        .apply(configure)
    return configuration.createEntityManagerFactory()
}
