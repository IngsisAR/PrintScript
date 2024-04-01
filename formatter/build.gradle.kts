plugins {
    kotlin("jvm")
    jacoco
}

group = "australfi.ingsis7"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    api(project(":utils"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
// tasks.jacocoTestCoverageVerification {
//    violationRules {
//        rule {
//            element = "BUNDLE"
//            limit {
//                counter = "INSTRUCTION"
//                value = "COVEREDRATIO"
//                minimum = "0.70".toBigDecimal()
//            }
//            limit {
//                counter = "BRANCH"
//                value = "COVEREDRATIO"
//                minimum = "0.69".toBigDecimal()
//            }
//        }
//    }
// }
//
// tasks.check {
//    dependsOn(tasks.jacocoTestCoverageVerification)
// }
kotlin {
    jvmToolchain(20)
}
