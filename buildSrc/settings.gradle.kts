dependencyResolutionManagement {
    versionCatalogs {
        @Suppress("UNUSED_VARIABLE") val libs by creating {
            from(files("../gradle/libs.versions.toml"))
        }
        @Suppress("UNUSED_VARIABLE") val testLibs by creating {
            from(files("../gradle/test-libs.versions.toml"))
        }
    }
}
