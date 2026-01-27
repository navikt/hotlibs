plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))

    implementation(libs.jackson.annotations)

    testImplementation(project(":serialization"))
}
