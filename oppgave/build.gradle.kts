plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))

    implementation(libs.bundles.jackson)
    implementation(libs.owasp.java.html.sanitizer)

    testImplementation(libs.bundles.test)
}
