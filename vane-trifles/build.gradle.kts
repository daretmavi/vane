plugins {
    alias(libs.plugins.shadow)
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    mavenCentral()
}

dependencies {
    compileOnly(libs.packetEvents)
    compileOnly(libs.json)
    implementation(kotlin("stdlib"))
    compileOnly(project(":vane-core"))
    testImplementation(kotlin("test"))
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
        relocate("org.json", "org.oddlama.vane.external.json")
        relocate("kotlin", "org.oddlama.vane.external.kotlin")
    }
}
kotlin {
    jvmToolchain(25)
}