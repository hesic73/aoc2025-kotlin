fun main() {
    val input = readInput("Day06")
    day06part1(input).println()
    day06part2(input).println()
}

fun day06part1(input: List<String>): Long {
    if (input.isEmpty()) {
        return 0
    }

    val maxWidth = input.maxOf { it.length }
    val padded = input.map { it.padEnd(maxWidth, ' ') }

    val columnGroups = findColumnGroups(padded)

    return columnGroups.sumOf { range ->
        val sliceRange = range.first..range.last
        val operation = padded.last().slice(sliceRange).first { it == '+' || it == '*' }
        val numbers = padded
            .dropLast(1)
            .map { row -> row.slice(sliceRange).trim() }
            .filter { it.isNotEmpty() }
            .map(String::toLong)

        when (operation) {
            '+' -> numbers.sum()
            '*' -> numbers.fold(1L) { acc, n -> acc * n }
            else -> error("Unexpected operation $operation")
        }
    }
}

fun day06part2(input: List<String>): Long {
    if (input.isEmpty()) {
        return 0
    }

    val maxWidth = input.maxOf { it.length }
    val padded = input.map { it.padEnd(maxWidth, ' ') }
    val columnGroups = findColumnGroups(padded)

    return columnGroups.sumOf { range ->
        val operation = padded.last().slice(range).first { it == '+' || it == '*' }

        val numbers = (range.last downTo range.first).mapNotNull { col ->
            val digits = buildString {
                for (row in 0 until padded.size - 1) {
                    val ch = padded[row][col]
                    if (ch != ' ') append(ch)
                }
            }
            digits.takeIf { it.isNotEmpty() }?.toLong()
        }

        when (operation) {
            '+' -> numbers.sum()
            '*' -> numbers.fold(1L) { acc, n -> acc * n }
            else -> error("Unexpected operation $operation")
        }
    }
}

private fun findColumnGroups(grid: List<String>): List<IntRange> {
    val width = grid.firstOrNull()?.length ?: 0

    val groups = mutableListOf<IntRange>()
    var start: Int? = null

    fun isBlankColumn(col: Int): Boolean = grid.all { it[col] == ' ' }

    for (col in 0 until width) {
        val blank = isBlankColumn(col)
        if (!blank && start == null) {
            start = col
        } else if (blank && start != null) {
            groups.add(start..<col)
            start = null
        }
    }

    if (start != null) {
        groups.add(start..<width)
    }

    return groups
}
