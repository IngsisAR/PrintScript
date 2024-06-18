package utils

class SystemInputProvider : InputProvider {
    override fun readInput(string: String): String {
        val input = readlnOrNull()
        return input.toString()
    }
}
