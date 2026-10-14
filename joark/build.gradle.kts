plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))

    implementation(libs.jackson.annotations)
    implementation(libs.urnlib)

    testImplementation(project(":serialization"))
}
