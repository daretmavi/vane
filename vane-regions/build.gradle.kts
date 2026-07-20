plugins {
    alias(libs.plugins.shadow)
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":vane-portals"))
    compileOnly(project(":vane-core"))
    compileOnly(libs.vault)
    compileOnly(libs.json)
    implementation(kotlin("stdlib"))
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
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}