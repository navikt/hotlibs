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
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }
