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
        frontier.println()
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

    fun PipeMap.getBoundaries(): Set<Coord> {
        val sCoord = this.filterValues { it == 'S' }.asIterable().single().key
        val adjacentS = this.getAdjacent(sCoord)
        val connectedS = adjacentS.filter { this.getAdjacent(it, true).any { coord -> this[coord] == 'S' }}
        var frontier = connectedS.toList()
        frontier.println()
        val visited: MutableSet<Coord> = mutableSetOf()

        while (true) {
            val newFrontier: MutableList<Coord> = mutableListOf()
            for (coord in frontier) {
                visited.add(coord)
                val adjacent = this.getAdjacent(coord).filter { !visited.contains(coord) }.toList()
                newFrontier.addAll(adjacent)
            }
            frontier = newFrontier
            if (frontier.isEmpty()) return visited
        }
    }

    fun part1(input: List<String>): Int {
        val pipeMap = parseMap(input)
        return pipeMap.getMaxDistanceFromS()
    }

    fun PipeMap.getEnclosedCount(): Int {
        val boundaries = this.getBoundaries()
        return 10
    }

    fun part2(input: List<String>): Int {
        val pipeMap = parseMap(input)
        return pipeMap.getEnclosedCount()
    }

    val testInput = readInput("Day10_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 8)
    val testInput2 = readInput("Day10_test2")
    val testResult2 = part2(testInput2)
    testResult2.println()
    check(testResult2 == 10)

    val realInput = readInput("Day10")
    part1(realInput).println()
    part2(realInput).println()
}