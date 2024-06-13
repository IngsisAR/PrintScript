package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

private const val VERSIONS = "utils/src/main/resources/versions.json"
data class Version(
    val version: String,
)

class VersionChecker {
    fun versionIsSameOrOlderThanCurrentVersion(
        target: String,
        currentVersion: String,
    ): Boolean {
        val targetVersionSplit = target.split(".").map { it.toInt() }
        val currentVersionSplit = currentVersion.split(".").map { it.toInt() }
        val currentVersionIsNewer = currentVersionSplit.zip(targetVersionSplit).all { (current, target) -> current >= target }
        return currentVersionIsNewer
    }

    fun versionIsValid(version: String): Boolean {
        return structureIsValid(version) && versionIsAvailable(version)
    }

    private fun structureIsValid(version: String): Boolean {
        val versionSplit = version.split(".")
        return versionSplit.size == 3
    }

    private fun versionIsAvailable(version: String): Boolean {
        val mapper = jacksonObjectMapper()
        val tokensRegex: List<Version> = mapper.readValue(File(VERSIONS))
        return tokensRegex.any { it.version == version }
    }
}
