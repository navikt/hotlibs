pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
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
    "core",
    "database",
    "http",
    "kafka",
    "katalog",
    "metrics",
    "nare",
    "rapids-and-rivers",
    "serialization",
)
