plugins {
    kotlin("jvm")
    jacoco
}

group = "australfi.ingsis7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies{
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(20)
}
