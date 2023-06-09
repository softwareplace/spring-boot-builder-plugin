rootProject.name = "example"

buildscript {
    System.setProperty("jdkVersion", "17")
}

include(":security")
include(":java-example")
include(":kotlin-example")

project(":security").projectDir = file("shared-modules/security")

includeBuild("../build-configuration")
includeBuild("../source/source-java")
includeBuild("../source/source-kotlin")

includeBuild("../openapi/openapi-java")
includeBuild("../openapi/openapi-kotlin")

apply(from = "../libs.settings.gradle.kts")


