typealias DesertMap = Map<String, Pair<String, String>>

fun main() {
    fun parseMapChoice(input: String): Pair<String, Pair<String, String>> {
        val splitByEquals = input.split("=").map(String::trim)
        val key = splitByEquals[0]
        val splitByComma = splitByEquals[1].split(",")
        val values = splitByComma.map { it.filter(Char::isLetterOrDigit) }
        return key to (values[0] to values[1])
    }
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    fun DesertMap.traverseByDirections(directions: List<Char>): Int {
        var cur = "AAA"
        var i = 0
        while (true) {
            for (dir in directions) {
                cur = when (dir) {
                    'L' -> this[cur]!!.first
                    'R' -> this[cur]!!.second
                    else -> error("invalid direction")
                }
                i++
                if (cur == "ZZZ") return i
            }
        }
    }

    fun DesertMap.traverseByGhostDirections(directions: List<Char>): Long {
        var cur = this.keys.filter { it.last() == 'A' }
        val first_z = cur.map { -1L } .toMutableList()
        var i = 0L
        while (true) {
            for (dir in directions) {
                cur = when (dir) {
                    'L' -> cur.map { this[it]!!.first }
                    'R' -> cur.map { this[it]!!.second }
                    else -> error("invalid direction")
                }
                i++
                for ((index, c) in cur.withIndex()) {
                    if (c.last() == 'Z' && first_z[index] == -1L) {
                        first_z[index] = i
                    }
                }
                if (first_z.all { it > 0}) {
                    return first_z.fold(1, ::lcm)
                }
            }
        }

    }

    fun part1(input: List<String>): Int {
        val directions = input.first().toList()
        val desertMap: DesertMap = input.drop(2).associate(::parseMapChoice)
        directions.println()
        desertMap.println()
        return desertMap.traverseByDirections(directions)
    }

    fun part2(input: List<String>): Long{
        val directions = input.first().toList()
        val desertMap = input.drop(2).associate(::parseMapChoice)
        return desertMap.traverseByGhostDirections(directions)
    }

    val testInput = readInput("Day08_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 2)
    val testInput2 = readInput("Day08_test2")
    val testResult2 = part2(testInput2)
    testResult2.println()
    check(testResult2 == 6L)

    val realInput = readInput("Day08")
    part1(realInput).println()
    part2(realInput).println()
}