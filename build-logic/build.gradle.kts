plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    // Gjør det mulig å bruke versjonskatalogen i convention plugins
    // se https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.spotless.gradle.plugin)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
