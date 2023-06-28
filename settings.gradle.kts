rootProject.name = "hm-http"

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/maven-release")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            name = "GitHubPackagesMirror"
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        }
    }
    versionCatalogs {
        create("libs") {
            from("no.nav.hjelpemidler:hm-katalog:0.0.8")
        }
    }
}
