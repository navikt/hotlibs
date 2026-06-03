plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))
    api(project(":http"))
}
