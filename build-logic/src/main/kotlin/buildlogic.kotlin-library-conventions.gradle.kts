plugins {
    id("buildlogic.kotlin-common-conventions")
    id("buildlogic.kotlin-publish-conventions")

    `java-library`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

java { withSourcesJar() }
