package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.jpa.AktørIdConverter
import no.nav.hjelpemidler.database.jpa.EnhetsnummerConverter
import no.nav.hjelpemidler.database.jpa.FødselsnummerConverter
import org.hibernate.SessionFactory

fun createSessionFactory(
    configure: SessionFactoryConfiguration.() -> Unit = {},
): SessionFactory {
    val configuration = SessionFactoryConfiguration()
        .apply {
            attributeConverter<AktørIdConverter>()
            attributeConverter<EnhetsnummerConverter>()
            attributeConverter<FødselsnummerConverter>()

            resourceLocal()
            snakeCase()
        }
        .apply(configure)
    return configuration.build()
}
