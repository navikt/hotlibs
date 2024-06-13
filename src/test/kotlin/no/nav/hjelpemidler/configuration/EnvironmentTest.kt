package no.nav.hjelpemidler.configuration

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class EnvironmentTest {
    @Test
    fun `Gjeldende miljø skal være local`() {
        Environment.current shouldBe LocalEnvironment
    }

    @Test
    fun `Hvert miljø har riktig tier`() {
        TestEnvironment shouldHaveTier Environment.Tier.LOCAL
        LocalEnvironment shouldHaveTier Environment.Tier.LOCAL
        FssEnvironment.DEV shouldHaveTier Environment.Tier.DEV
        FssEnvironment.PROD shouldHaveTier Environment.Tier.PROD
        GcpEnvironment.DEV shouldHaveTier Environment.Tier.DEV
        GcpEnvironment.PROD shouldHaveTier Environment.Tier.PROD
    }

    @Test
    fun `Hver tier har riktig flagg`() {
        Environment.Tier.LOCAL.shouldHaveFlag(isLocal = true)
        Environment.Tier.DEV.shouldHaveFlag(isDev = true)
        Environment.Tier.PROD.shouldHaveFlag(isProd = true)
    }

    private infix fun Environment.shouldHaveTier(tier: Environment.Tier) {
        this.tier shouldBe tier
    }

    private fun Environment.Tier.shouldHaveFlag(
        isLocal: Boolean = false,
        isDev: Boolean = false,
        isProd: Boolean = false,
    ) = assertSoftly {
        this.isLocal shouldBe isLocal
        this.isDev shouldBe isDev
        this.isProd shouldBe isProd
    }
}
