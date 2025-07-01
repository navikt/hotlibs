package no.nav.hjelpemidler.database.repository

import jakarta.persistence.Tuple
import no.nav.hjelpemidler.database.DatabaseRecord
import kotlin.reflect.KClass

class RepositoryRecord internal constructor(private val tuple: Tuple) : DatabaseRecord {
    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    fun <T : Any> get(position: Int, type: KClass<T>): T =
        tuple.get(position, type.java)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    inline fun <reified T : Any> get(position: Int): T =
        get(position, T::class)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    fun <T : Any> getOrNull(position: Int, type: KClass<T>): T? =
        tuple.get(position, type.java)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    inline fun <reified T : Any> getOrNull(position: Int): T? =
        get(position, T::class)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    fun <T : Any> get(alias: String, type: KClass<T>): T =
        tuple.get(alias, type.java)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    inline fun <reified T : Any> get(alias: String): T =
        get(alias, T::class)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    fun <T : Any> getOrNull(alias: String, type: KClass<T>): T? =
        tuple.get(alias, type.java)

    /**
     * @see [jakarta.persistence.Tuple.get]
     */
    inline fun <reified T : Any> getOrNull(alias: String): T? =
        get(alias, T::class)

    inline fun <reified E : Enum<E>> enum(position: Int): E =
        enumValueOf<E>(get<String>(position))

    inline fun <reified E : Enum<E>> enum(alias: String): E =
        enumValueOf<E>(get<String>(alias))

    inline fun <reified E : Enum<E>> enumOrNull(position: Int): E? =
        getOrNull<String>(position)?.let { enumValueOf<E>(it) }

    inline fun <reified E : Enum<E>> enumOrNull(alias: String): E? =
        getOrNull<String>(alias)?.let { enumValueOf<E>(it) }

    /**
     * @see [jakarta.persistence.Tuple.getElements]
     * @see [jakarta.persistence.Tuple.get]
     */
    override fun toMap(): Map<String, Any?> =
        tuple.elements.associate { it.alias to tuple.get(it) }
}

fun Tuple.toRecord(): RepositoryRecord = RepositoryRecord(this)
