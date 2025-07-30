package no.nav.hjelpemidler.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.boolex.OnMarkerEvaluator
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.filter.EvaluatorFilter
import ch.qos.logback.core.spi.FilterReply
import io.github.oshai.kotlinlogging.logback.toLogback
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.encoder.LogstashEncoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import kotlin.test.BeforeTest
import kotlin.test.Test

class LoggingConfiguratorTest {
    private val context = LoggerFactory.getILoggerFactory() as LoggerContext

    private val regularLoggingEvent =
        mockk<ILoggingEvent> { every { markerList } returns emptyList<Marker>() }
    private val sensitiveLoggingEvent =
        mockk<ILoggingEvent> { every { markerList } returns listOf(teamLogsMarker.toLogback()) }

    @BeforeTest
    fun setUp() {
        context.reset()
    }

    @Test
    fun `Verifiser TEST_AUTOCONFIG_FILE`() {
        LoggingConfigurator(LoggingConfigurator.TEST_AUTOCONFIG_FILE).also { it.configure(context) }

        assertSoftly(context.getLogger(Logger.ROOT_LOGGER_NAME)) {
            isAdditive shouldBe true
            level shouldBe Level.WARN

            shouldHaveAppenderNamed<ConsoleAppender<*>>("appLog").should {
                it.encoder.shouldBeInstanceOf<PatternLayoutEncoder>()
                it.shouldFilter(regularLoggingEvent, FilterReply.NEUTRAL)
                it.shouldFilter(sensitiveLoggingEvent, FilterReply.DENY)
            }

            shouldHaveAppenderNamed<ConsoleAppender<*>>("teamLogs").should {
                it.encoder.shouldBeInstanceOf<PatternLayoutEncoder>()
                it.shouldFilter(regularLoggingEvent, FilterReply.DENY)
                it.shouldFilter(sensitiveLoggingEvent, FilterReply.NEUTRAL)
            }
        }

        assertSoftly(context.getLogger("tjenestekall")) {
            isAdditive shouldBe false
            level shouldBe Level.WARN

            shouldHaveAppenderNamed<ConsoleAppender<*>>("secureLog").should {
                it.encoder.shouldBeInstanceOf<PatternLayoutEncoder>()
                it.copyOfAttachedFiltersList.shouldBeEmpty()
            }
        }
    }

    @Test
    fun `Verifiser AUTOCONFIG_FILE`() {
        LoggingConfigurator(LoggingConfigurator.AUTOCONFIG_FILE).also { it.configure(context) }

        assertSoftly(context.getLogger(Logger.ROOT_LOGGER_NAME)) {
            isAdditive shouldBe true
            level shouldBe Level.INFO

            shouldHaveAppenderNamed<ConsoleAppender<*>>("appLog").should {
                it.encoder.shouldBeInstanceOf<LogstashEncoder>()
                it.shouldFilter(regularLoggingEvent, FilterReply.NEUTRAL)
                it.shouldFilter(sensitiveLoggingEvent, FilterReply.DENY)
            }

            shouldHaveAppenderNamed<LogstashTcpSocketAppender>("teamLogs").should {
                it.encoder.shouldBeInstanceOf<LogstashEncoder>()
                it.shouldFilter(regularLoggingEvent, FilterReply.DENY)
                it.shouldFilter(sensitiveLoggingEvent, FilterReply.NEUTRAL)
            }
        }

        assertSoftly(context.getLogger("tjenestekall")) {
            isAdditive shouldBe false
            level shouldBe Level.INFO

            shouldHaveAppenderNamed<LogstashTcpSocketAppender>("secureLog").should {
                it.encoder.shouldBeInstanceOf<LogstashEncoder>()
                it.copyOfAttachedFiltersList.shouldBeEmpty()
            }
        }
    }
}

private inline fun <reified T : Appender<*>> ch.qos.logback.classic.Logger.shouldHaveAppenderNamed(name: String): T =
    getAppender(name).shouldBeInstanceOf<T>()

private fun Appender<*>.shouldFilter(event: ILoggingEvent, reply: FilterReply) =
    copyOfAttachedFiltersList.shouldBeSingleton {
        val filter = it.shouldBeInstanceOf<EvaluatorFilter<ILoggingEvent>>()
        filter.evaluator.shouldBeInstanceOf<OnMarkerEvaluator>()
        filter.decide(event) shouldBe reply
    }
