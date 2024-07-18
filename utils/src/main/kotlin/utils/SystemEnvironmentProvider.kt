package utils

class SystemEnvironmentProvider : EnvironmentProvider {
    override fun getEnv(name: String): String? {
        return System.getenv(name)
    }
}
