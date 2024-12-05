plugins {
    `maven-publish`
    `version-catalog`
}

group = "no.nav.hjelpemidler"
version = System.getenv("VERSION_TAG") ?: System.getenv("GITHUB_REF_NAME") ?: "local"

buildscript {
    dependencies {
        classpath(libs.commons.text)
    }
}

repositories {
    gradlePluginPortal()
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))

        // Legg til hotlibs i katalogen med versjonen som bygges nå
        val hotlibs = version("hotlibs", "$version")
        listOf("core", "database", "http", "kafka", "rapids-and-rivers", "nare").forEach { artifact ->
            library("$hotlibs-${toCamelCase(artifact)}", "$group", artifact).versionRef(hotlibs)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/hotlibs")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

fun toCamelCase(value: String): String = org.apache.commons.text.CaseUtils.toCamelCase(value, false, '-')
