import no.nav.hjelpemidler.gradle.CurrentGitBranchValueSource

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

fun snapshot(version: String) = "$version-SNAPSHOT"

val currentBranchName = providers
    .of(CurrentGitBranchValueSource::class.java) {}
    .orElse("unknown")

// NB! Defineres i navikt/hm-workflows/.github/workflows/publish-jar.yaml
val releaseVersion = providers.environmentVariable("VERSION_TAG")

group = "no.nav.hjelpemidler"
version = providers
    .environmentVariablesPrefixedBy("GITHUB_")
    .flatMap {
        val isCI = it["GITHUB_ACTIONS"] == "true"
        val refType = it["GITHUB_REF_TYPE"] // null | "branch" | "tag"
        val refName = it["GITHUB_REF_NAME"] // null | "main"   | "v1.0"
        val refNameProvider = providers.provider { refName }
        when {
            !isCI -> currentBranchName.map(::snapshot)
            refType == "tag" -> refNameProvider
            refName == "main" -> releaseVersion
            else -> refNameProvider.map(::snapshot)
        }
    }
    .get()
