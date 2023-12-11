data class Coord(val x: Int, val y: Int)
typealias PipeMap = Map<Coord, Char>

fun main() {
    fun parseMap(input: List<String>): PipeMap {
        val seq: Sequence<Pair<Coord, Char>> = sequence {
            for ((y, line) in input.withIndex()) {
                for ((x, char) in line.withIndex()) {
                    yield(Coord(x, y) to char)
                }
            }
        }
        return seq.toMap()
    }

    fun PipeMap.getAdjacent(loc: Coord, allowS: Boolean = false): Sequence<Coord> {
        val adjacencyOffsets = when (this[loc]) {
            'S' -> sequenceOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            '|' -> sequenceOf(0 to -1, 0 to 1)
            '-' -> sequenceOf(-1 to 0, 1 to 0)
            'L' -> sequenceOf(0 to -1, 1 to 0)
            'J' -> sequenceOf(-1 to 0, 0 to -1)
            '7' -> sequenceOf(-1 to 0, 0 to 1)
            'F' -> sequenceOf(1 to 0, 0 to 1)
            else -> sequenceOf()
        }
        return adjacencyOffsets.map { Coord(loc.x + it.first, loc.y + it.second)}
            .filter { allowS || this[it] != 'S' }
    }

    fun PipeMap.getMaxDistanceFromS(): Int {
        val sCoord = this.filterValues { it == 'S' }.asIterable().single().key
        val adjacentS = this.getAdjacent(sCoord)
        val connectedS = adjacentS.filter { this.getAdjacent(it, true).any { coord -> this[coord] == 'S' }}
        var frontier = connectedS.toList()
        val visited: MutableMap<Coord, Int> = mutableMapOf()
        var distance = 1
        while (true) {
            val newFrontier: MutableList<Coord> = mutableListOf()
            for (coord in frontier) {
                visited[coord] = distance
                val adjacent = this.getAdjacent(coord).filter { visited[it] == null }.toList()
                newFrontier.addAll(adjacent)
            }
            frontier = newFrontier
            distance += 1
            if (frontier.isEmpty()) return visited.maxOf { it.value }
        }
    }

    fun PipeMap.getSCoord(): Coord = this.filterValues { it == 'S' }.asIterable().single().key
    fun PipeMap.getSConnected(): List<Coord> {
        val adjacentS = this.getAdjacent(this.getSCoord())
        val connectedS = adjacentS.filter { this.getAdjacent(it, true).any { coord -> this[coord] == 'S' }}
        return connectedS.toList()
    }

    fun PipeMap.getBoundaries(): Set<Coord> {
        val sCoord = this.getSCoord()
        val connectedS = this.getSConnected()
        var frontier = connectedS.toList()
        val visited: MutableSet<Coord> = mutableSetOf()

        while (true) {
            val newFrontier: MutableList<Coord> = mutableListOf()
            for (coord in frontier) {
                visited.add(coord)
                val adjacent = this.getAdjacent(coord).filter { !visited.contains(it) }.toList()
                newFrontier.addAll(adjacent)
            }
            frontier = newFrontier
            if (frontier.isEmpty()) {
                visited.add(sCoord)
                return visited}
        }
    }

    fun PipeMap.pointIsEnclosed(point: Coord, boundary: Set<Coord>): Boolean {
        val rayToLeft = (0..<(point.x)).map { Coord(it, point.y)}
        val boundaryIntersections = rayToLeft.filter {boundary.contains(it)}
        var realCrossings = 0
        var lastCrossing: Int? = null // Y-axis direction of last crossing if corner i.e. F is 1, J is -1
        fun handleCrossing(yDelta: Int) {
            if (lastCrossing != null) {
                if (lastCrossing?.plus(yDelta) == 0) {
                    realCrossings += 1
                }
                lastCrossing = null
            } else {
                lastCrossing = yDelta
            }
        }
        fun handleS() {
            val sCoord = getSCoord()
            val sConnected = getSConnected()
            if (sConnected.count{ it.y != sCoord.y} == 2) {
                realCrossings += 1
            } else if (sConnected.count { it.y != sCoord.y } == 1) {
                handleCrossing(sConnected.maxOf { it.y } - sCoord.y)
            }
        }
        /*rayToLeft.println()
        boundaryIntersections.println()*/
        for (coord in boundaryIntersections) {
            when (this[coord]) {
                '|' -> realCrossings += 1
                'F' -> handleCrossing(1)
                'J' -> handleCrossing(-1)
                '7' -> handleCrossing(1)
                'L' -> handleCrossing(-1)
                '-' -> {}
                'S' -> handleS()
                else -> error("Didn't expect this")
            }
        }
        return realCrossings % 2 == 1
    }

    fun part1(input: List<String>): Int {
        val pipeMap = parseMap(input)
        return pipeMap.getMaxDistanceFromS()
    }

    fun PipeMap.getEnclosedCount(): Int {
        val boundaries = this.getBoundaries()
        // boundaries.println()
        val nonBoundary = this.filterKeys { !boundaries.contains(it)}
        val enclosedPoints = nonBoundary.filterKeys { this.pointIsEnclosed(it, boundaries) }.filterValues { it != 'S' }
        return enclosedPoints.count()
    }

    fun part2(input: List<String>): Int {
        val pipeMap = parseMap(input)
        return pipeMap.getEnclosedCount()
    }

    val testInput = readInput("Day10_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 8)
    val testInput3 = readInput("Day10_test3")
    val testResult3 = part2(testInput3)
    testResult3.println()
    check(testResult3 == 8)
    val testInput2 = readInput("Day10_test2")
    val testResult2 = part2(testInput2)
    testResult2.println()
    check(testResult2 == 10)
    val testInput4 = readInput("Day10_test4")
    val testResult4 = part2(testInput4)
    testResult4.println()
    check(testResult4 == 4)

    val realInput = readInput("Day10")
    part1(realInput).println()
    part2(realInput).println()
}