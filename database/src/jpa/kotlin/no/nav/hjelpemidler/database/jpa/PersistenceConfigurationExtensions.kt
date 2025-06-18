package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.PersistenceConfiguration
import org.hibernate.cfg.AvailableSettings
import javax.sql.DataSource
import kotlin.reflect.KClass

fun <T : Any> PersistenceConfiguration.managedClass(kClass: KClass<T>): PersistenceConfiguration =
    managedClass(kClass.java)

inline fun <reified T : Any> PersistenceConfiguration.managedClass(): PersistenceConfiguration =
    managedClass(T::class)

fun PersistenceConfiguration.dataSource(dataSource: DataSource): PersistenceConfiguration =
    property(AvailableSettings.JAKARTA_NON_JTA_DATASOURCE, dataSource)
