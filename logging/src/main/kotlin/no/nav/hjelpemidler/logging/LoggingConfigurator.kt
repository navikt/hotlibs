package no.nav.hjelpemidler.logging

import ch.qos.logback.classic.ClassicConstants
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.Configurator.ExecutionStatus
import ch.qos.logback.classic.spi.ConfiguratorRank
import ch.qos.logback.core.spi.ContextAwareBase
import java.net.URL

/**
 * [ch.qos.logback.classic.spi.Configurator] for å sikre at Logback ikke benytter logback.xml fra andre
 * biblioteker, f.eks. rapids-and-rivers.
 */
@ConfiguratorRank(value = ConfiguratorRank.CUSTOM_TOP_PRIORITY)
internal class LoggingConfigurator(private val resourceName: String? = null) : ContextAwareBase(), Configurator {
    override fun configure(context: LoggerContext): ExecutionStatus {
        val url = resourceName?.let(::getResource)
            ?: getResource(TEST_AUTOCONFIG_FILE)
            ?: getResource(AUTOCONFIG_FILE)
            ?: error("$AUTOCONFIG_FILE mangler!")
        JoranConfigurator().also {
            it.context = context
            it.doConfigure(url)
        }
        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
    }

    private fun getResource(name: String): URL? = javaClass.classLoader.getResource(name)

    companion object {
        /**
         * @see [ch.qos.logback.classic.ClassicConstants.AUTOCONFIG_FILE]
         */
        const val AUTOCONFIG_FILE = "logback-hotlibs.xml"
        const val TEST_AUTOCONFIG_FILE = ClassicConstants.TEST_AUTOCONFIG_FILE
    }
}
