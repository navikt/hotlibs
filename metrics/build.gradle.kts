plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))

    // InfluxDB
    implementation(libs.influxdb.client.kotlin)
}
