package no.nav.hjelpemidler.database.repository

import jakarta.persistence.PersistenceUnitTransactionType
import org.hibernate.SessionFactory
import org.hibernate.cfg.JdbcSettings
import org.hibernate.cfg.MappingSettings
import org.hibernate.jpa.HibernatePersistenceConfiguration
import javax.sql.DataSource
import kotlin.reflect.KClass

class RepositoryConfiguration private constructor(private val configuration: HibernatePersistenceConfiguration) {
    constructor(name: String = "default") : this(HibernatePersistenceConfiguration(name))

    fun property(name: String, value: Any) {
        configuration.property(name, value)
    }

    fun dataSource(dataSource: DataSource) {
        property(JdbcSettings.JAKARTA_NON_JTA_DATASOURCE, dataSource)
    }

    fun resourceLocal() {
        configuration.transactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
    }

    fun snakeCase() {
        property(
            MappingSettings.PHYSICAL_NAMING_STRATEGY,
            "org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl"
        )
    }

    fun <T : Any> managedClass(managedClass: KClass<T>) {
        configuration.managedClass(managedClass.java)
    }

    inline fun <reified T : Any> managedClass() {
        managedClass(T::class)
    }

    fun showSql(showSql: Boolean, formatSql: Boolean, highlightSql: Boolean) {
        configuration.showSql(showSql, formatSql, highlightSql)
    }

    internal fun createEntityManagerFactory(): SessionFactory = configuration.createEntityManagerFactory()
}
