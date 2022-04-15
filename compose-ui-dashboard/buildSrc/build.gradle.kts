plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.6.10")
    implementation(gradleApi())
    implementation(localGroovy())
}

gradlePlugin {
    plugins {
        create("customFrcPlugins") {
            id = "nt-plugin"
            implementationClass = "org.aztechs.NetworkTablesPlugin"
        }
    }
}

