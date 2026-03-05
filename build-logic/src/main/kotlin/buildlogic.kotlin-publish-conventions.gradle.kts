import java.time.OffsetDateTime
import java.time.ZoneId
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

val zoneId: ZoneId = ZoneId.of("Europe/Oslo")
val versionFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.DDD.HHmmss")

group = "no.nav.hjelpemidler"
version = run {
    val isGitHubActions = System.getenv("GITHUB_ACTIONS") == "true"
    val refType = System.getenv("GITHUB_REF_TYPE")
    val refName = System.getenv("GITHUB_REF_NAME")

    val baseVersion = OffsetDateTime.now(zoneId).format(versionFormatter)

    when {
        !isGitHubActions -> "$baseVersion-SNAPSHOT"
        refType == "tag" && refName != null -> refName
        refName == "main" -> baseVersion
        else -> "$baseVersion-SNAPSHOT"
    }
}
