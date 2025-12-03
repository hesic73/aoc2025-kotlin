import kotlin.math.pow

fun main() {
    val input = readInput("Day02")
    day02part1(input).println()
    day02part2(input).println()
}

fun hasRepeatingPattern(s: String): Boolean {
    val len = s.length
    for (patternLen in 1..len / 2) {
        if (len % patternLen != 0) {
            continue
        }
        var match = true
        for (i in patternLen until len) {
            if (s[i] != s[i % patternLen]) {
                match = false
                break
            }
        }
        if (match) {
            return true
        }
    }
    return false
}

fun day02part1(input: List<String>): Long {
    val line = input.joinToString("")
    val ranges = line.trimEnd(',').split(",")

    var sum = 0L

    for (range in ranges) {
        val (start, end) = range.split("-").map { it.toLong() }

        val startLen = start.toString().length
        val endLen = end.toString().length

        for (len in startLen..endLen) {
            if (len % 2 != 0) {
                continue
            }

            val halfLen = len / 2
            val minHalf = if (halfLen == 1) 1L else 10.0.pow((halfLen - 1).toDouble()).toLong()
            val maxHalf = 10.0.pow(halfLen.toDouble()).toLong() - 1

            for (half in minHalf..maxHalf) {
                val invalid = (half.toString() + half.toString()).toLong()
                if (invalid in start..end) {
                    sum += invalid
                }
            }
        }
    }

    return sum
}

fun day02part2(input: List<String>): Long {
    val line = input.joinToString("")
    val ranges = line.trimEnd(',').split(",")

    var sum = 0L

    for (rangeStr in ranges) {
        val (startStr, endStr) = rangeStr.split("-")
        val start = startStr.toLong()
        val end = endStr.toLong()

        for (num in start..end) {
            val numStr = num.toString()
            if (hasRepeatingPattern(numStr)) {
                sum += num
            }
        }
    }

    return sum
}
