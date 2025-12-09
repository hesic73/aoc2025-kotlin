import kotlin.math.abs

fun main() {
    val input = readInput("Day09")
    day09part1(input).println()
    day09part2(input).println()
}

private data class Point2D(val x: Int, val y: Int)

private fun day09part1(input: List<String>): Long {
    val points = input.map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        Point2D(x, y)
    }

    var maxArea = 0L
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val (p1, p2) = points[i] to points[j]
            val area = (abs(p1.x - p2.x) + 1L) * (abs(p1.y - p2.y) + 1L)
            if (area > maxArea) maxArea = area
        }
    }
    return maxArea
}

private fun day09part2(input: List<String>): Long {
    val polygon = input.map { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        Point2D(x, y)
    }

    val edgePoints = buildSet {
        addAll(polygon)
        polygon.indices.forEach { i ->
            val p1 = polygon[i]
            val p2 = polygon[(i + 1) % polygon.size]
            addEdgePoints(p1, p2)
        }
    }

    val keyYCoords = polygon.map { it.y }.distinct().sorted()
    val validXRanges = keyYCoords.associateWith { y ->
        computeValidXRanges(y, edgePoints, polygon)
    }

    var maxArea = 0L
    for (i in polygon.indices) {
        for (j in i + 1 until polygon.size) {
            val (p1, p2) = polygon[i] to polygon[j]
            val (minX, maxX) = minOf(p1.x, p2.x) to maxOf(p1.x, p2.x)
            val (minY, maxY) = minOf(p1.y, p2.y) to maxOf(p1.y, p2.y)
            val area = (maxX - minX + 1L) * (maxY - minY + 1L)

            if (area > maxArea && isRectangleValid(minX, maxX, minY, maxY, keyYCoords, validXRanges, edgePoints, polygon)) {
                maxArea = area
            }
        }
    }
    return maxArea
}

private fun isRectangleValid(
    minX: Int, maxX: Int, minY: Int, maxY: Int,
    keyYCoords: List<Int>,
    validXRanges: Map<Int, List<IntRange>>,
    edgePoints: Set<Point2D>,
    polygon: List<Point2D>
): Boolean {
    val yToCheck = keyYCoords.filter { it in minY..maxY }
    if (yToCheck.isEmpty()) return false

    for (y in yToCheck) {
        if (!isRangeCovered(minX, maxX, validXRanges[y]!!)) return false
    }

    val ySamples = buildList {
        if (yToCheck.first() > minY) add((minY + yToCheck.first()) / 2)
        for (k in 0 until yToCheck.size - 1) {
            val (y1, y2) = yToCheck[k] to yToCheck[k + 1]
            if (y2 - y1 > 1) add((y1 + y2) / 2)
        }
        if (yToCheck.last() < maxY) add((yToCheck.last() + maxY) / 2)
    }

    for (y in ySamples) {
        val ranges = computeValidXRanges(y, edgePoints, polygon)
        if (!isRangeCovered(minX, maxX, ranges)) return false
    }

    return true
}

private fun computeValidXRanges(y: Int, edgePoints: Set<Point2D>, polygon: List<Point2D>): List<IntRange> {
    val edgeXs = edgePoints.filter { it.y == y }.map { it.x }.sorted()
    if (edgeXs.isEmpty()) return emptyList()

    return buildList {
        var i = 0
        while (i < edgeXs.size) {
            val start = edgeXs[i]
            var end = start
            while (i + 1 < edgeXs.size && edgeXs[i + 1] == end + 1) {
                i++
                end = edgeXs[i]
            }
            add(start..end)

            if (i + 1 < edgeXs.size) {
                val nextStart = edgeXs[i + 1]
                if (nextStart > end + 1 && isPointInPolygon(Point2D((end + nextStart) / 2, y), polygon)) {
                    add((end + 1) until nextStart)
                }
            }
            i++
        }
    }
}

private fun isRangeCovered(min: Int, max: Int, ranges: List<IntRange>): Boolean {
    if (ranges.isEmpty()) return false

    val merged = ranges.sortedBy { it.first }.fold(mutableListOf<IntRange>()) { acc, range ->
        if (acc.isEmpty() || range.first > acc.last().last + 1) {
            acc.add(range)
        } else {
            acc[acc.lastIndex] = acc.last().first..maxOf(acc.last().last, range.last)
        }
        acc
    }

    return merged.any { min >= it.first && max <= it.last }
}

private fun MutableSet<Point2D>.addEdgePoints(p1: Point2D, p2: Point2D) {
    when {
        p1.x == p2.x -> (minOf(p1.y, p2.y)..maxOf(p1.y, p2.y)).forEach { add(Point2D(p1.x, it)) }
        p1.y == p2.y -> (minOf(p1.x, p2.x)..maxOf(p1.x, p2.x)).forEach { add(Point2D(it, p1.y)) }
    }
}

private fun isPointInPolygon(point: Point2D, polygon: List<Point2D>): Boolean {
    var inside = false
    polygon.indices.forEach { i ->
        val p1 = polygon[i]
        val p2 = polygon[(i + 1) % polygon.size]
        if ((p1.y > point.y) != (p2.y > point.y)) {
            val xIntersect = (p2.x - p1.x) * (point.y - p1.y) / (p2.y - p1.y) + p1.x
            if (point.x < xIntersect) inside = !inside
        }
    }
    return inside
}
