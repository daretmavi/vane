plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.blossom)
    kotlin("jvm")
    kotlin("kapt")
}

sourceSets {
    main {
        blossom {
            javaSources {
                property($$"$VERSION", project.version.toString())
            }
        }
    }
}

dependencies {
    implementation(libs.nightConfig)
    implementation(libs.slf4j)
    compileOnly(libs.json)
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("com.electronwill.night-config:toml"))
        }

        relocate("com.electronwill.night-config", "org.oddlama.vane.vane_velocity.external.night-config")
        relocate("org.json", "org.oddlama.vane.external.json")
    }
}
repositories {
    mavenCentral()
}
