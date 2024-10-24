pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "hotlibs"

include(
    "core",
    "database",
    "http",
    "kafka",
    "katalog",
    "nare",
)
