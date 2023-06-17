plugins {
    `kotlin-dsl`
    `maven-publish`
    `version-catalog`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME")

repositories {
    gradlePluginPortal()
}

catalog {
    versionCatalog {
        from(files("./gradle/libs.versions.toml"))
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/hm-katalog")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])
        }
    }
}
