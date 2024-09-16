pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "hotlibs"

include(
    "core",
    "database",
    "http",
    "katalog",
    "nare",
)
