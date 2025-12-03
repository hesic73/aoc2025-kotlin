fun main() {
    val input = readInput("Day03")
    day03part1(input).println()
    day03part2(input).println()
}

fun day03part1(input: List<String>): Long {
    return input.sumOf { line ->
        findMaxTwoDigit(line).toLong()
    }
}

fun findMaxTwoDigit(line: String): Int {
    var max = 0
    val n = line.length
    for (i in 0 until n) {
        val digit1 = line[i].digitToInt()
        for (j in i + 1 until n) {
            val digit2 = line[j].digitToInt()
            val current = digit1 * 10 + digit2
            if (current > max) {
                max = current
            }
        }
    }
    return max
}

fun day03part2(input: List<String>): Long {
    val digitsToKeep = 12
    return input.sumOf { line ->
        findMaxKDigit(line, digitsToKeep).toLong()
    }
}

fun findMaxKDigit(input: String, k: Int): String {
    val n = input.length
    if (k == n) return input
    if (k == 0) return ""
    val stack = mutableListOf<Char>()
    var digitsToRemove = n - k
    for (char in input) {
        while (digitsToRemove > 0 && stack.isNotEmpty() && stack.last() < char) {
            stack.removeLast()
            digitsToRemove--
        }
        stack.add(char)
    }
    return stack.take(k).joinToString("")
}
