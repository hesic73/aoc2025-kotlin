fun main() {
    val input = readInput("Day05")
    val (freshRanges, availableIds) = parseDay05Input(input)

    day05part1(freshRanges, availableIds).println()
    day05part2(freshRanges).println()
}

fun day05part1(ranges: List<LongRange>, ids: List<Long>): Int =
    ids.count { id -> ranges.any { id in it } }

fun day05part2(ranges: List<LongRange>): Long =
    mergeRanges(ranges).sumOf { it.last - it.first + 1 }

private fun parseDay05Input(lines: List<String>): Pair<List<LongRange>, List<Long>> {
    val blankLineIndex = lines.indexOf("")
    val rawRanges = lines.subList(0, blankLineIndex)
    val ids = lines.drop(blankLineIndex + 1).map(String::toLong)

    val ranges = rawRanges
        .map { line ->
            val (start, end) = line.split("-").map(String::toLong)
            start..end
        }
        .sortedBy { it.first }

    return ranges to ids
}

private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
    if (ranges.isEmpty()) return emptyList()

    val merged = mutableListOf(ranges.first())

    for (current in ranges.drop(1)) {
        val last = merged.last()

        if (current.first <= last.last + 1) {
            val combined = last.first..maxOf(last.last, current.last)
            merged[merged.lastIndex] = combined
        } else {
            merged.add(current)
        }
    }

    return merged
}
