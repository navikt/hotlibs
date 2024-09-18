plugins {
    `maven-publish`
    `version-catalog`
}

group = "no.nav.hjelpemidler"
version = System.getenv("VERSION_TAG") ?: System.getenv("GITHUB_REF_NAME") ?: "local"

repositories {
    gradlePluginPortal()
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))

        // Legg til hotlibs i katalogen med versjonen som bygges nå
        val hotlibs = version("hotlibs", "$version")
        listOf("core", "database", "http", "nare").forEach { artifact ->
            library("$hotlibs-$artifact", "$group", artifact).versionRef(hotlibs)
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
