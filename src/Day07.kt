import java.util.ArrayDeque

fun main() {
    val input = readInput("Day07")
    day07part1(input).println()
    day07part2(input).println()
}

fun day07part1(input: List<String>): Long {
    val startCol = input[0].indexOf('S')
    if (startCol == -1) {
        return 0L
    }

    var splits = 0L

    val beams = ArrayDeque<Pair<Int, Int>>()

    beams.add(Pair(1, startCol))

    val visitedInNextRow = mutableSetOf<Pair<Int, Int>>()

    while (beams.isNotEmpty()) {
        val (row, col) = beams.removeFirst()

        if (row >= input.size || col < 0 || col >= input[0].length) {
            continue
        }

        if (input[row][col] == '^') {
            splits++
            if (visitedInNextRow.add(Pair(row + 1, col - 1))) {
                beams.add(Pair(row + 1, col - 1))
            }
            if (visitedInNextRow.add(Pair(row + 1, col + 1))) {
                beams.add(Pair(row + 1, col + 1))
            }
        } else {
            if (visitedInNextRow.add(Pair(row + 1, col))) {
                beams.add(Pair(row + 1, col))
            }
        }

        if (beams.none { it.first == row }) {
            visitedInNextRow.clear()
        }
    }

    return splits
}

fun day07part2(input: List<String>): Long {
    val startCol = input[0].indexOf('S')
    if (startCol == -1) {
        return 0L
    }
    val memo = mutableMapOf<Pair<Int, Int>, Long>()
    fun countTimelines(row: Int, col: Int): Long {
        if (row >= input.size) {
            return 1L
        }
        if (col < 0 || col >= input[0].length) {
            return 0L
        }
        val pos = Pair(row, col)
        if (memo.containsKey(pos)) {
            return memo[pos]!!
        }
        val result = if (input[row][col] == '^') {
            countTimelines(row + 1, col - 1) + countTimelines(row + 1, col + 1)
        } else {
            countTimelines(row + 1, col)
        }
        memo[pos] = result
        return result
    }
    return countTimelines(1, startCol)
}
