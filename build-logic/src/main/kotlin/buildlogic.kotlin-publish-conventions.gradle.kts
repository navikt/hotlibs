import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

val isGitHubActions: Boolean = System.getenv("GITHUB_ACTIONS") == "true"
val refType: String? = System.getenv("GITHUB_REF_TYPE")
val refName: String? = System.getenv("GITHUB_REF_NAME")

val snapshotVersion by lazy {
    val yearDayFormatter = DateTimeFormatter.ofPattern("yy.DDD")
    val yearDay = LocalDate.now().format(yearDayFormatter)
    "$yearDay-SNAPSHOT"
}

group = "no.nav.hjelpemidler"
version = when {
    !isGitHubActions -> snapshotVersion
    refType == "tag" && refName != null -> refName
    refName == "main" -> System.getenv("VERSION_TAG")
    else -> snapshotVersion
}
