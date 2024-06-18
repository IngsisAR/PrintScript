package utils

class PrintOutputProvider : OutputProvider {
    override fun print(string: String) {
        println(string)
    }
}
