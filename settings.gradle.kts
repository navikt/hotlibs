pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://packages.confluent.io/maven/")
        maven {
            url = uri("https://maven.pkg.github.com/navikt/*")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        }
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
