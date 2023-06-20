package com.gradle.kts.build.java.openapi

import com.gradle.kts.build.configuration.Dependencies
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.extensions.OpenApiGeneratorGenerateExtension

enum class DocumentationProvider(val type: String) {
    SPRING_DOC("springdoc"),
    NONE("none"),
    SOURCE("source")
}

class OpenApiSettings(
    var generator: String = "spring",
    var sourceFolder: String = "rest",
    var modelNameSuffix: String = "Rest",
    var swaggerFileName: String = "openapi.yaml",
)

val openApiSettings: OpenApiSettings by lazy { OpenApiSettings() }

private const val JAVA_LOCAL_DATE_TIME = "java.time.LocalDateTime"
private const val JAVA_LOCAL_DATE = "java.time.LocalDate"
private const val JAVA_LOCAL_TIME = "java.time.LocalTime"


fun OpenApiGeneratorGenerateExtension.apply(
    groupId: String = "app",
    projectPath: String,
    openApiYamlFilePath: String = "${projectPath}/src/main/resources/${openApiSettings.swaggerFileName}"
) {
    schemaMappings.putAll(
        mapOf(
            "date-time" to JAVA_LOCAL_DATE_TIME
        )
    )
    importMappings.set(
        mapOf(
            "java.time.OffsetDateTime" to "java.time.LocalDateTime"
        )
    )
    typeMappings.set(
        mapOf(
            "date" to JAVA_LOCAL_DATE,
            "local-date-time" to JAVA_LOCAL_DATE_TIME,
            "time" to JAVA_LOCAL_TIME
        )
    )

    generatorName.set(openApiSettings.generator)
    this.groupId.set("${groupId}.${openApiSettings.sourceFolder}")
    packageName.set("${groupId}.${openApiSettings.sourceFolder}")
    inputSpec.set(openApiYamlFilePath)
    generateApiDocumentation.set(true)
    outputDir.set("${projectPath}/build/generated")
    apiPackage.set("${groupId}.${openApiSettings.sourceFolder}.controller")
    invokerPackage.set("${groupId}.${openApiSettings.sourceFolder}.invoker")
    apiNameSuffix.set("Controller")
    modelNameSuffix.set(openApiSettings.modelNameSuffix)
    modelPackage.set("${groupId}.${openApiSettings.sourceFolder}.model")
    skipOperationExample.set(true)
    configOptions.set(
        mapOf(
            "apiSuffix" to "Controller",
            "apiNameSuffix" to "Controller",
            "additionalModelTypeAnnotations" to "@lombok.Builder\n@lombok.Data",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "defaultInterfaces" to "false",
            "delegatePattern" to "false",
            "documentationProvider" to DocumentationProvider.SPRING_DOC.type,
            "serializationLibrary" to "jackson",
            "gradleBuildFile" to "false",
            "enumPropertyNaming" to "original",
            "exceptionHandler" to "false",
            "useSpringBoot3" to "true",
            "useSwaggerUI" to "true",
            "useTags" to "true",
            "generateApis" to "true",
            "java8" to "true"
        )
    )
}

fun Project.openApiGenerateConfig() {
    afterEvaluate {
        extensions.getByName<OpenApiGeneratorGenerateExtension>("openApiGenerate").apply(
            groupId = "$group",
            projectPath = projectDir.path,
        )
    }
}

fun Project.applyJavaSourceSets() {
    extra["snippetsDir"] = file("build/generated-snippets")
    extensions.getByName<SourceSetContainer>("sourceSets").apply {
        getByName("main").apply {
            java {
                srcDir("$projectDir/build/generated/src/main/java")
                exclude(
                    "**/ApiUtil.java",
                )
            }
        }
    }
}

fun Project.applyTasks() {
    tasks.withType<KotlinCompile> {
        dependsOn(tasks.findByName("openApiGenerate"))
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = Dependencies.Version.jdk
        }
    }
}
