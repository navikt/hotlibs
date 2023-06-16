plugins {
    `kotlin-dsl`
    `version-catalog`
    `maven-publish`
}

repositories {
    gradlePluginPortal()
}

catalog {
    versionCatalog {
        library("hm-katalog", "no.nav.hjelpemidler:hm-katalog:0.1")
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
}
