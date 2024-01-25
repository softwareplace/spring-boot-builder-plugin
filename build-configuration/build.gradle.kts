import org.gradle.api.JavaVersion.toVersion

plugins {
    `maven-publish`
    `kotlin-dsl`
    kotlin("jvm") version System.getProperty("kotlinVersion")
}

val sourceGroup = "com.gradle.kts.build.configuration"
val currentVersion = "1.0.0"

group = sourceGroup
version = currentVersion

beforeEvaluate {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
        maven("https://repo.spring.io/milestone")
    }
}

afterEvaluate {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
        maven("https://repo.spring.io/milestone")
    }
}

beforeEvaluate {
    java {
        withJavadocJar()
        withSourcesJar()
        sourceCompatibility = toVersion(System.getProperty("jdkVersion"))
        targetCompatibility = toVersion(System.getProperty("jdkVersion"))

        toolchain {
            languageVersion.set(JavaLanguageVersion.of(System.getProperty("jdkVersion")))
        }
    }
}

afterEvaluate {
    allprojects {
        java {
            withJavadocJar()
            withSourcesJar()
            sourceCompatibility = toVersion(System.getProperty("jdkVersion"))
            targetCompatibility = toVersion(System.getProperty("jdkVersion"))

            toolchain {
                languageVersion.set(JavaLanguageVersion.of(System.getProperty("jdkVersion")))
            }
        }
    }
}

gradlePlugin {
    plugins {
        register("build-configuration-plugins") {
            id = "build-configuration-plugin"
            implementationClass = "$sourceGroup.BuildConfigurationPlugin"
            version = currentVersion
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${System.getProperty("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${System.getProperty("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${System.getProperty("kotlinVersion")}")
}


