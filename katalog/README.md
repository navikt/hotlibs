# Katalog

Felles [Gradle Version Catalog](https://docs.gradle.org/current/userguide/platforms.html) for DigiHoT.

NB! Katalogfilen finner du her [/gradle/libs.versions.toml](/gradle/libs.versions.toml).

## Eksempel på bruk

```kotlin
// settings.gradle.kts

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        // for autentisert tilgang til GitHub Packages
        maven {
            url = uri("https://maven.pkg.github.com/navikt/*")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        // public mirror for GitHub Packages
        maven {
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
        }
    }
    versionCatalogs {
        create("libs") {
            from("no.nav.hjelpemidler:katalog:24.260.161343")
        }
    }
}
```

```kotlin
// build.gradle.kts

dependencies {
    implementation(libs.hotlibs.core)
    implementation(libs.hotlibs.http)
}
```
