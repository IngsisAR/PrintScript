package utils

class SystemInputProvider : InputProvider {
    override fun readInput(): String {
        val input = readlnOrNull()
        return input.toString()
    }
}
