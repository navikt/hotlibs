pluginManagement {
    includeBuild("build-logic")
}

fun RepositoryHandler.github(repository: String) {
    maven {
        url = uri("https://maven.pkg.github.com/$repository")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven")

        github("navikt/hm-contract-pdl-avro")
        github("navikt/rapids-and-rivers")
        github("navikt/tbd-libs")
        github("navikt/tms-ktor-token-support")
        github("navikt/tms-varsel-authority")

        // Plassert under GitHub-repositories (med authentication) for å unngå unødvendige kostnader.
        maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}

rootProject.name = "hotlibs"

include(
    "behovsmelding",
    "core",
    "database",
    "http",
    "kafka",
    "katalog",
    "logging",
    "nare",
    "oppgave",
    "platform",
    "rapids-and-rivers",
    "serialization",
    "streams",
    "test",
)
