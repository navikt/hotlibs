package no.nav.hjelpemidler.security

import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import java.security.Principal
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface AuthenticatedPrincipal : Principal {
    val id: StringId

    override fun getName(): String = id.value
}

@JvmInline
value class AuthenticatedApplication(override val id: Applikasjonsnavn) : AuthenticatedPrincipal {
    override fun toString(): String = id.value
}

interface AuthenticatedUser : AuthenticatedPrincipal {
    val userToken: String
}

inline fun <T> AuthenticatedPrincipal.mapApplicationOrNull(block: (AuthenticatedApplication) -> T): T? =
    if (this is AuthenticatedApplication) block(this) else null

inline fun <T> AuthenticatedPrincipal.mapUserOrNull(block: (AuthenticatedUser) -> T): T? =
    if (this is AuthenticatedUser) block(this) else null

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireApplication(): AuthenticatedApplication {
    contract {
        returns() implies (this@requireApplication is AuthenticatedApplication)
    }
    return this as? AuthenticatedApplication
        ?: error("Expected AuthenticatedApplication, got ${this::class.qualifiedName}")
}

@OptIn(ExperimentalContracts::class)
fun AuthenticatedPrincipal.requireUser(): AuthenticatedUser {
    contract {
        returns() implies (this@requireUser is AuthenticatedUser)
    }
    return this as? AuthenticatedUser
        ?: error("Expected AuthenticatedUser, got ${this::class.qualifiedName}")
}

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isApplication: Boolean
    get() {
        contract {
            returns(true) implies (this@isApplication is AuthenticatedApplication)
        }
        return this is AuthenticatedApplication
    }

@OptIn(ExperimentalContracts::class)
val AuthenticatedPrincipal.isUser: Boolean
    get() {
        contract {
            returns(true) implies (this@isUser is AuthenticatedUser)
        }
        return this is AuthenticatedUser
    }
