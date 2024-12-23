plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    implementation(libs.influxdb.client.kotlin)
    implementation(libs.kotlin.logging)
    testRuntimeOnly(libs.slf4j.simple)
}
