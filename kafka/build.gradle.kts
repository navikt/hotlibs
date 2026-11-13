plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(libs.kafka.clients)
    constraints {
        implementation("com.github.luben:zstd-jni:1.5.7-26")
    }
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.annotations)
    implementation(libs.kotlinx.coroutines.core)

    compileOnly(libs.kafka.avro.serializer)
}
