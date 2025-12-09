
fun main() {
    val input = readInput("Day08")
    day08part1(input).println()
    day08part2(input).println()
}

private data class Point3D(val x: Long, val y: Long, val z: Long)

private fun Point3D.squaredDistance(other: Point3D): Long {
    val dx = this.x - other.x
    val dy = this.y - other.y
    val dz = this.z - other.z
    return dx * dx + dy * dy + dz * dz
}

private data class Edge(val u: Int, val v: Int, val distance: Long)

private class UnionFind(size: Int) {
    private val parent = IntArray(size) { it }
    private val sz = IntArray(size) { 1 }
    var count = size
        private set

    fun find(i: Int): Int {
        if (parent[i] == i) return i
        parent[i] = find(parent[i])
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val rootI = find(i)
        val rootJ = find(j)
        if (rootI != rootJ) {
            if (sz[rootI] < sz[rootJ]) {
                parent[rootI] = rootJ
                sz[rootJ] += sz[rootI]
            } else {
                parent[rootJ] = rootI
                sz[rootI] += sz[rootJ]
            }
            count--
        }
    }

    fun getCircuitSizes(): List<Int> {
        val roots = parent.indices.map(::find).toSet()
        return roots.map { sz[it] }
    }
}


fun day08part1(input: List<String>): Long {
    val point3DS = input.map {
        val (x, y, z) = it.split(",").map(String::toLong)
        Point3D(x, y, z)
    }

    val edges = mutableListOf<Edge>()
    for (i in point3DS.indices) {
        for (j in i + 1 until point3DS.size) {
            edges.add(Edge(i, j, point3DS[i].squaredDistance(point3DS[j])))
        }
    }

    edges.sortBy { it.distance }

    val uf = UnionFind(point3DS.size)
    for (i in 0 until 1000) {
        uf.union(edges[i].u, edges[i].v)
    }

    val sizes = uf.getCircuitSizes().sortedDescending()
    return sizes[0].toLong() * sizes[1].toLong() * sizes[2].toLong()
}

fun day08part2(input: List<String>): Long {
    val point3DS = input.map {
        val (x, y, z) = it.split(",").map(String::toLong)
        Point3D(x, y, z)
    }

    val edges = mutableListOf<Edge>()
    for (i in point3DS.indices) {
        for (j in i + 1 until point3DS.size) {
            edges.add(Edge(i, j, point3DS[i].squaredDistance(point3DS[j])))
        }
    }

    edges.sortBy { it.distance }

    val uf = UnionFind(point3DS.size)
    for (edge in edges) {
        if (uf.find(edge.u) != uf.find(edge.v)) {
            uf.union(edge.u, edge.v)
            if (uf.count == 1) {
                val point1 = point3DS[edge.u]
                val point2 = point3DS[edge.v]
                return point1.x * point2.x
            }
        }
    }

    return 0L
}
