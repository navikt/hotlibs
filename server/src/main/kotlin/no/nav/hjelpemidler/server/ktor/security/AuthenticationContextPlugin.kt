package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.Hook
import io.ktor.server.application.call
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.routing.Route
import io.ktor.util.pipeline.PipelinePhase
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.security.AuthenticationContext

private val AuthenticationContextPlugin = createRouteScopedPlugin("AuthenticationContextPlugin") {
    on(AfterAuthentication) { call, proceed ->
        val principal = call.authenticatedPrincipal()
        if (principal == null) {
            proceed()
        } else {
            withContext(AuthenticationContext(principal)) { proceed() }
        }
    }
}

private object AfterAuthentication : Hook<suspend (ApplicationCall, suspend () -> Unit) -> Unit> {
    private val AfterAuthenticationPhase: PipelinePhase = PipelinePhase("AfterAuthentication")
    override fun install(
        pipeline: ApplicationCallPipeline,
        handler: suspend (ApplicationCall, suspend () -> Unit) -> Unit,
    ) {
        pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, AfterAuthenticationPhase)
        pipeline.intercept(AfterAuthenticationPhase) { handler(call, ::proceed) }
    }
}

fun Route.authenticationContext() {
    install(AuthenticationContextPlugin)
}
