pluginManagement {
    plugins {
        kotlin("multiplatform") version "1.6.10"
        id("org.jetbrains.compose") version "1.1.0"
    }

    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://frcmaven.wpi.edu/artifactory/release/")
    }
}

rootProject.name = "compose-ui-dashboard"


include(":desktop")
include(":common")

