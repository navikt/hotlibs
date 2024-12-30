plugins {
    `maven-publish`
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/hotlibs")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

group = "no.nav.hjelpemidler"
version = System.getenv("VERSION_TAG") ?: System.getenv("GITHUB_REF_NAME") ?: "local"
