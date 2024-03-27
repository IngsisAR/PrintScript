plugins {
    kotlin("jvm")
    jacoco
}

group = "australfi.ingsis7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(20)
}
