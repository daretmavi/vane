plugins {
    kotlin("jvm")
    kotlin("kapt")
}
dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(25)
}