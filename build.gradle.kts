plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
}

versionCatalogUpdate {
    sortByKey = true
    pin {
        versions.addAll(
            "kotest",
            "kotlin",
            "protobuf",
            "slf4j",
            "tbdLibs",
        )
    }
    keep {
        keepUnusedVersions = true
        keepUnusedPlugins = true
        keepUnusedLibraries = true
    }
}
