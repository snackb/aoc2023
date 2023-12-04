

fun main() {

    fun part1(input: List<String>): Int {
        return 0
    }
    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day%DAY%_test.txt")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 0)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 0)

    val realInput = readInput("Day%DAY%.txt")
    part1(realInput).println()
    part2(realInput).println()
}