plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "PrintScript"
include("cli", "interpreter", "lexer", "parser", "utils", "formatter")
include("sca")
