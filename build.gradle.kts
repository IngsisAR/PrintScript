val ktlint by configurations.creating

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "australfi.ingsis7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.2.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

val ktlintFormat by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

tasks.check {
    dependsOn(ktlintFormat)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}
