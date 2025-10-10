package no.nav.hjelpemidler.database

import io.kotest.matchers.collections.shouldContainAll
import java.util.TreeSet
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.test.Ignore
import kotlin.test.Test

class RowTest {
    @Test
    @Ignore
    fun `Lag delegerte funksjoner`() {
        Row::class.declaredMemberFunctions
            .filter { it.parameters.size > 1 }
            .filterNot {
                it.name in setOf(
                    "aktørId",
                    "aktørIdOrNull",
                    "fødselsnummer",
                    "fødselsnummerOrNull",
                )
            }
            .forEach {
                val parameters = it.parameters
                    .drop(1)
                    .associate { parameter ->
                        (parameter.name ?: "") to ((parameter.type.classifier as KClass<*>).simpleName ?: "")
                    }
                val parameters1 = parameters.entries.joinToString(", ") { (name, type) -> "$name: $type" }
                val parameters2 = parameters.keys.joinToString(", ")
                val functionName = it.name
                val returnType = (it.returnType.classifier as? KClass<*>)?.simpleName ?: return@forEach
                val returnTypeNullable = if (it.returnType.isMarkedNullable) "$returnType?" else returnType
                println("\tfun $functionName($parameters1): $returnTypeNullable =\n\t\twrapped.$functionName($parameters2)\n")
            }
    }

    @Test
    fun `Sjekk at alle funksjoner er definert`() {
        val rowFunctions = Row::class.declaredMemberFunctions
            .associateBy { it.name }

        val wrappedFunction = kotliquery.Row::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterNot {
                it.name in setOf(
                    "metaDataOrNull",
                    "statementOrNull",
                    "warningsOrNull",

                    "close",
                    "component1",
                    "component2",
                    "copy",
                    "equals",
                    "hashCode",
                    "iterator",
                    "next",
                    "toString",
                )
            }
            .filterNot { it.name.startsWith("joda") }
            .mapTo(TreeSet()) { it.name }

        rowFunctions.keys.shouldContainAll(wrappedFunction)
    }
}
