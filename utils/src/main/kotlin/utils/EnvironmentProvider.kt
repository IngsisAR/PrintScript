package utils

interface EnvironmentProvider {
    fun getEnv(name: String): String?
}
