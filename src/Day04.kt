fun main() {
    val input = readInput("Day04")
    day04part1(input).println()
    day04part2(input).println()
}

fun day04part1(input: List<String>): Int {
    var count = 0

    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == '@') {
                val adjacentCount = countAdjacent(i, j) { row, col ->
                    input.getOrNull(row)?.getOrNull(col) == '@'
                }
                if (adjacentCount < 4) {
                    count++
                }
            }
        }
    }

    return count
}

fun day04part2(input: List<String>): Int {
    val grid = input.map { it.toCharArray() }.toTypedArray()
    var totalRemoved = 0

    while (true) {
        val positionsToRemove = mutableListOf<Pair<Int, Int>>()

        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '@') {
                    val adjacentCount = countAdjacent(i, j) { row, col ->
                        grid.getOrNull(row)?.getOrNull(col) == '@'
                    }
                    if (adjacentCount < 4) {
                        positionsToRemove.add(i to j)
                    }
                }
            }
        }

        if (positionsToRemove.isEmpty()) {
            break
        }
        
        totalRemoved += positionsToRemove.size
        for ((i, j) in positionsToRemove) {
            grid[i][j] = '.'
        }
    }

    return totalRemoved
}

private fun countAdjacent(row: Int, col: Int, hasPaper: (Int, Int) -> Boolean): Int {
    var count = 0
    val directions = listOf(
        -1 to -1, -1 to 0, -1 to 1,
        0 to -1,          0 to 1,
        1 to -1,  1 to 0,  1 to 1
    )

    for ((dr, dc) in directions) {
        val newRow = row + dr
        val newCol = col + dc
        if (hasPaper(newRow, newCol)) {
            count++
        }
    }

    return count
}
