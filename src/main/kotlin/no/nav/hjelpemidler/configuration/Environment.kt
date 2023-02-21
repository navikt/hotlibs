package no.nav.hjelpemidler.configuration

interface Environment {
    val cluster: String

    companion object {
        private val all: Set<Environment> = setOf(LocalEnvironment) +
                setOf(*FssEnvironment.values()) +
                setOf(*GcpEnvironment.values())

        fun current(): Environment {
            val cluster = System.getenv("NAIS_CLUSTER_NAME")
            return all.find {
                it.cluster == cluster
            } ?: LocalEnvironment
        }
    }
}

object LocalEnvironment : Environment {
    override val cluster: String = "local"
    override fun toString(): String = cluster
}

enum class FssEnvironment(override val cluster: String) : Environment {
    DEV("dev-fss"),
    PROD("prod-fss");

    override fun toString(): String = cluster
}

enum class GcpEnvironment(override val cluster: String) : Environment {
    DEV("dev-gcp"),
    PROD("prod-gcp"),
    LABS("labs-gcp");

    override fun toString(): String = cluster
}
