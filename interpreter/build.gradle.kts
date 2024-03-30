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
}

tasks.test {
    useJUnitPlatform()
}
//tasks.jacocoTestCoverageVerification {
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
//}
//
//tasks.check {
//    dependsOn(tasks.jacocoTestCoverageVerification)
//}
kotlin {
    jvmToolchain(20)
}
