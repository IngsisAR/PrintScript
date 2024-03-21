plugins {
    kotlin("jvm")
}

group = "australfi.ingsis7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}
