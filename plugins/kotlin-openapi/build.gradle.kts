import com.github.softwareplace.springboot.plugin.buildconfiguration.Dependencies
import com.github.softwareplace.springboot.plugin.buildconfiguration.implementation
import org.springframework.boot.gradle.tasks.run.BootRun


plugins {
    `maven-publish`
    `kotlin-dsl`
    id("com.github.softwareplace.springboot.plugin.kotlin")
    id("org.openapi.generator") version System.getProperty("openApiToolsVersion")
}

val sourceGroup = "com.github.softwareplace.springboot.plugin"

group = sourceGroup

publishing {
    publications {
        create<MavenPublication>("springBootOpenapiPlugin") {
            groupId = sourceGroup
            artifactId = "kotlin-openapi"
            java.sourceCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))
            java.targetCompatibility = JavaVersion.toVersion(System.getProperty("jdkVersion"))

            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}

gradlePlugin {
    plugins {
        register("kotlin-openapi") {
            id = "com.github.softwareplace.springboot.plugin.kotlin-openapi"
            implementationClass = "$sourceGroup.kotlinopenapi.OpenApiPlugin"
        }
    }
}

tasks.named<Jar>("bootJar").configure {
    enabled = false
}

tasks.named<BootRun>("bootRun").configure {
    enabled = false
}

dependencies {
    System.setProperty("kotlin-spring", "${projectDir}/src/main/resources/kotlin-spring")
    implementation("com.github.softwareplace.springboot.plugin:build-configuration:${System.getProperty("pluginsVersion")}")
    implementation("org.openapitools:openapi-generator-gradle-plugin:${Dependencies.Version.openApiToolsVersion}") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
}

