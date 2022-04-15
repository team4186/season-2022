import org.aztechs.nt
import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("nt-plugin")
}

group = "org.aztechs"
version = "1.0"

kotlin {
    jvm("desktop") {
        compilations.all { kotlinOptions.jvmTarget = "11" }
        withJava()
        testRuns["test"].executionTask.configure { useJUnit() }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(nt.core)
                api(nt.wpiUtil)
                api(nt.currentOs)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
        val desktopTest by getting
    }
}
