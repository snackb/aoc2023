

fun main() {

    fun getWinningCounts(time: Long, distance: Long): Long {
        return (1..time).count {
            (time - it) * it > distance
        }.toLong()
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(":")[1].split(" ").mapNotNull(String::toLongOrNull)
        val distances = input[1].split(":")[1].split(" ").mapNotNull(String::toLongOrNull)
        times.println()
        distances.println()
        var pairs = times.zip(distances)
        var result = pairs.map { (time, distance) -> getWinningCounts(time, distance)}
        return result.fold(1, Long::times)
    }
    fun part2(input: List<String>): Long {
        val time = input[0].filter { it.isDigit() }.toLong()
        val distance = input[1].filter {it.isDigit() }.toLong()
        return getWinningCounts(time, distance)
    }

    val testInput = readInput("Day06_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 288L)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 71503L)

    val realInput = readInput("Day06")
    part1(realInput).println()
    part2(realInput).println()
}