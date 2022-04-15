package org.aztechs

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal val ntVersion get() = "2022.4.1"
class NetworkTablesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("hello") {
            doLast {
                println("Hello from the GreetingPlugin")
            }
        }
    }

    object Dependencies {
        val core = "edu.wpi.first.ntcore:ntcore-java:$ntVersion"
        val wpiUtil = "edu.wpi.first.wpiutil:wpiutil-java:$ntVersion"
        val linuxX64 = ntDependency("linuxx86-64")
        val windowsX64 = ntDependency("windowsx86-64")
        val macosX64 = ntDependency("osxx86-64")

        val currentOs by lazy {
            val os = System.getProperty("os.name")
            when {
                os.startsWith("Linux", ignoreCase = true) -> linuxX64
                os.startsWith("Win", ignoreCase = true) -> windowsX64
                os.equals("Mac OS X", ignoreCase = true) -> macosX64
                else -> error("Unknown OS name: $os")
            }
        }
    }
}

val KotlinDependencyHandler.nt get() = NetworkTablesPlugin.Dependencies
val DependencyHandler.nt get() = NetworkTablesPlugin.Dependencies

private fun ntDependency(platform: String) = "edu.wpi.first.ntcore:ntcore-jni:$ntVersion:$platform"