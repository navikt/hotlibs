plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.version.catalog.update)
}

versionCatalogUpdate {
    sortByKey = true
    keep {
        keepUnusedVersions = true
    }
}
