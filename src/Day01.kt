fun main() {

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int {
    var position = 50
    var count = 0

    for (line in input) {
        val direction = line[0]
        val distance = line.substring(1).toInt()

        position = when (direction) {
            'L' -> (position - distance).mod(100)
            'R' -> (position + distance).mod(100)
            else -> position
        }

        if (position == 0) count++
    }

    return count
}

fun part2(input: List<String>): Int {
    var position = 50
    var count = 0

    for (line in input) {
        val direction = line[0]
        val distance = line.substring(1).toInt()

        if (direction == 'L') {
            if (position == 0) {
                count += distance / 100
            } else {

                count += (distance + (100 - position)) / 100
            }
            position = (position - distance).mod(100)
        } else {
            count += (position + distance) / 100
            position = (position + distance).mod(100)
        }
    }

    return count
}
