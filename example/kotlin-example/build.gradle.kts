import com.gradle.kts.kotlin.buildsource.*

plugins {
    id("source-plugin")
    id("openapi-plugin")
}

group = "com.kotlin.example.openapi"
version = "1.0.0"

dependencies {
    implementation(project(":security"))
    kotlinReactive()
    springJettyApi()
    mappstruct()
    jsonLogger()
    test()
}

