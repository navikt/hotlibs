package no.nav.hjelpemidler.configuration

import java.util.EnumSet

sealed interface Environment {
    val cluster: String
    val tier: Tier

    companion object {
        private val all: Set<Environment> = setOf(TestEnvironment, LocalEnvironment) +
                EnumSet.allOf(FssEnvironment::class.java) +
                EnumSet.allOf(GcpEnvironment::class.java)

        val current: Environment by lazy {
            val cluster = System.getenv("NAIS_CLUSTER_NAME")
            all.find { it.cluster == cluster } ?: LocalEnvironment
        }
    }

    val isTest: Boolean get() = tier.isTest
    val isLocal: Boolean get() = tier.isLocal
    val isDev: Boolean get() = tier.isDev
    val isProd: Boolean get() = tier.isProd

    enum class Tier {
        TEST, LOCAL, DEV, PROD;

        val isTest: Boolean get() = this == TEST
        val isLocal: Boolean get() = this == LOCAL
        val isDev: Boolean get() = this == DEV
        val isProd: Boolean get() = this == PROD
    }
}

sealed class DefaultEnvironment() : Environment {
    override fun toString(): String = cluster
}

object TestEnvironment : DefaultEnvironment() {
    override val cluster: String = "test"
    override val tier: Environment.Tier = Environment.Tier.TEST
}

object LocalEnvironment : DefaultEnvironment() {
    override val cluster: String = "local"
    override val tier: Environment.Tier = Environment.Tier.LOCAL
}

sealed interface ClusterEnvironment : Environment

enum class FssEnvironment(
    override val cluster: String,
    override val tier: Environment.Tier,
) : ClusterEnvironment {
    DEV("dev-fss", Environment.Tier.DEV),
    PROD("prod-fss", Environment.Tier.PROD);

    override fun toString(): String = cluster
}

enum class GcpEnvironment(
    override val cluster: String,
    override val tier: Environment.Tier,
) : ClusterEnvironment {
    DEV("dev-gcp", Environment.Tier.DEV),
    PROD("prod-gcp", Environment.Tier.PROD);

    override fun toString(): String = cluster
}
