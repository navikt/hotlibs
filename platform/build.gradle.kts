plugins {
    id("buildlogic.kotlin-publish-conventions")

    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform(libs.jackson.bom))
    api(platform(libs.jackson2.bom))
    api(platform(libs.kotlin.bom))
    api(platform(libs.kotlinx.coroutines.bom))
    api(platform(libs.kotlinx.serialization.bom))
    api(platform(libs.ktor.bom))
    api(platform(libs.netty.bom))
    api(platform(libs.protobuf.bom))
    api(platform(libs.slf4j.bom))

    constraints {
        api(libs.commons.codec)
        api(libs.commons.compress)
        api(libs.commons.io)
        api(libs.commons.lang3)
        api(libs.commons.text)
        api(libs.guava)
        api(libs.java.jwt)
        api(libs.logback.classic)
        api(libs.logback.core)
        api(libs.opentelemetry.api)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["javaPlatform"])
        }
    }
}
