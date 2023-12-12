import kotlin.math.absoluteValue

fun main() {
    fun parseMap(input: List<String>): Map<Coord, Char> {
        return sequence {
            for ((y, line) in input.withIndex()) {
                for ((x, char) in line.withIndex()) {
                    if (char == '#') yield(Pair(Coord(x, y), char))
                }
            }
        }.toMap()
    }

    fun part1(input: List<String>): Int {
        val galaxyMap = parseMap(input)
        val vacantX = (0..<input[0].length).filter { xCoord -> galaxyMap.none { it.key.x == xCoord } }
        val vacantY = (0..<input.count()).filter { yCoord -> galaxyMap.none { it.key.y == yCoord } }
        val mapExpandedX = vacantX.foldRight(galaxyMap) { xCoord, galaxies ->
            galaxies.mapKeys {
                if (it.key.x > xCoord) Coord(it.key.x + 1, it.key.y) else it.key
            }
        }
        val mapExpandedBoth = vacantY.foldRight(mapExpandedX) { yCoord, galaxies ->
            galaxies.mapKeys {
                if (it.key.y > yCoord) Coord(it.key.x, it.key.y + 1) else it.key
            }
        }
        val indexed = mapExpandedBoth.keys.withIndex()
        var distanceSum = 0
        var pairs = 0
        for ((i, first) in indexed) {
            for ((j, second) in indexed) {
                if (i < j) {
                    val distance = (second.x - first.x).absoluteValue + (second.y - first.y).absoluteValue
                    distanceSum += distance
                    pairs += 1
                }
            }
        }
        return distanceSum
    }


    fun part2(input: List<String>, factor: Int): Long{
        val galaxyMap = parseMap(input)
        val vacantX = (0..<input[0].length).filter { xCoord -> galaxyMap.none { it.key.x == xCoord } }
        val vacantY = (0..<input.count()).filter { yCoord -> galaxyMap.none { it.key.y == yCoord } }
        val mapExpandedX = vacantX.foldRight(galaxyMap) { xCoord, galaxies ->
            galaxies.mapKeys {
                if (it.key.x > xCoord) Coord(it.key.x + factor, it.key.y) else it.key
            }
        }
        val mapExpandedBoth = vacantY.foldRight(mapExpandedX) { yCoord, galaxies ->
            galaxies.mapKeys {
                if (it.key.y > yCoord) Coord(it.key.x, it.key.y + factor) else it.key
            }
        }
        val indexed = mapExpandedBoth.keys.withIndex()
        var distanceSum = 0L
        var pairs = 0
        for ((i, first) in indexed) {
            for ((j, second) in indexed) {
                if (i < j) {
                    val distance = (second.x - first.x).absoluteValue + (second.y - first.y).absoluteValue
                    distanceSum += distance
                    pairs += 1
                }
            }
        }
        return distanceSum
    }


    val testInput = readInput("Day11_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 374)
    val testResult2 = part2(testInput, 100-1)
    testResult2.println()
    check(testResult2 == 8410L)

    val realInput = readInput("Day11")
    part1(realInput).println()
    part2(realInput, 1000000-1).println()
}