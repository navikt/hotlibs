plugins {
    id("buildlogic.kotlin-publish-conventions")

    `version-catalog`
}

buildscript {
    dependencies {
        classpath(libs.commons.text)
    }
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))

        // Legg til hotlibs i katalogen med versjonen som bygges nå
        val hotlibs = version("hotlibs", "$version")
        listOf(
            "core",
            "database",
            "http",
            "kafka",
            "logging",
            "metrics",
            "nare",
            "platform",
            "rapids-and-rivers",
            "serialization",
            "test",
        ).forEach { artifact ->
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
}

fun toCamelCase(value: String): String = org.apache.commons.text.CaseUtils.toCamelCase(value, false, '-')
