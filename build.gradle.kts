import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ktlint)
}

ktlint {
    verbose.set(true)
    coloredOutput.set(true)
    outputToConsole.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("src/"))
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        licenseHeader(
            """
            // Copyright (c) ${'$'}YEAR Elshad Safarov
            //
            // SPDX-License-Identifier: MIT
            
            """.trimIndent() + "\n",
        )
    }
}

repositories {
    mavenCentral()
}

group = "io.github.elshad98"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(libs.picocli)
    kapt(libs.picocli.codegen)

    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.compileKotlin {
    dependsOn("ktlintCheck")
}