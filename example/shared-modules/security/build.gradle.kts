import com.github.softwareplace.plugin.kotlinbuildsource.jsonLogger
import com.github.softwareplace.plugin.kotlinbuildsource.loggBack

plugins {
    id("com.github.softwareplace.plugin.spring-boot-submodule-source-plugin")
}

dependencies {
    loggBack()
    jsonLogger()
}

