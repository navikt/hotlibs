package no.nav.hjelpemidler.database.hibernate

import jakarta.persistence.AttributeConverter
import jakarta.persistence.PersistenceUnitTransactionType
import org.hibernate.SessionFactory
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl
import org.hibernate.cfg.AvailableSettings
import org.hibernate.cfg.Configuration
import javax.sql.DataSource
import kotlin.reflect.KClass

class SessionFactoryConfiguration internal constructor(private val configuration: Configuration = Configuration()) {
    fun annotatedClass(managedClass: KClass<*>) {
        configuration.addAnnotatedClass(managedClass.java)
    }

    inline fun <reified T : Any> annotatedClass() {
        annotatedClass(T::class)
    }

    fun annotatedClasses(vararg managedClasses: KClass<*>) {
        managedClasses.forEach(::annotatedClass)
    }

    fun attributeConverter(attributeConverter: AttributeConverter<*, *>, autoApply: Boolean = true) {
        configuration.addAttributeConverter(attributeConverter, autoApply)
    }

    fun <T : AttributeConverter<*, *>> attributeConverter(
        attributeConverterClass: KClass<T>,
        autoApply: Boolean = true,
    ) {
        configuration.addAttributeConverter(attributeConverterClass.java, autoApply)
    }

    inline fun <reified T : AttributeConverter<*, *>> attributeConverter(autoApply: Boolean = true) {
        attributeConverter(T::class, autoApply)
    }

    fun attributeConverters(vararg attributeConverters: AttributeConverter<*, *>) {
        attributeConverters.forEach(::attributeConverter)
    }

    fun dataSource(dataSource: DataSource) {
        configuration.properties[AvailableSettings.JAKARTA_NON_JTA_DATASOURCE] = dataSource
    }

    fun resourceLocal() {
        configuration.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
    }

    fun showSql(showSql: Boolean = false, formatSql: Boolean = false, highlightSql: Boolean = false) {
        configuration.showSql(showSql, formatSql, highlightSql)
    }

    fun snakeCase() {
        configuration.setPhysicalNamingStrategy(PhysicalNamingStrategySnakeCaseImpl())
    }

    internal fun build(): SessionFactory = configuration.buildSessionFactory()
}
