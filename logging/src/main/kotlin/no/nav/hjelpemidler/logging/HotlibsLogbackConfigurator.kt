package no.nav.hjelpemidler.logging

import ch.qos.logback.classic.ClassicConstants
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.Configurator.ExecutionStatus
import ch.qos.logback.core.joran.spi.JoranException
import ch.qos.logback.core.spi.ContextAwareBase

/**
 * Custom [Configurator] for å sikre at Logback ikke benytter logback.xml fra andre biblioteker, f.eks.
 * rapids-and-rivers.
 */
class HotlibsLogbackConfigurator : ContextAwareBase(), Configurator {
    private val logbackXml = "logback-hotlibs.xml"

    override fun configure(context: LoggerContext): ExecutionStatus {
        val configUrl = javaClass.classLoader.getResource(ClassicConstants.TEST_AUTOCONFIG_FILE)
            ?: javaClass.classLoader.getResource(logbackXml)
            ?: error("$logbackXml mangler!")
        try {
            val configurator = JoranConfigurator()
            configurator.context = context
            configurator.doConfigure(configUrl)
        } catch (e: JoranException) {
            throw e
        }
        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }
}
