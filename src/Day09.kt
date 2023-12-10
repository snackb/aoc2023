

fun main() {
    fun predict(input: List<Int>): Int {
        val layers: MutableList<List<Int>> = mutableListOf(input)
        while (!layers.last().all { it == 0}) {
            layers += layers.last().zipWithNext().map {(l, r) -> r - l}
        }
        return layers.foldRight(0) { list, acc ->
            list.last() + acc
        }
    }
    fun predictFirst(input: List<Int>): Int {
        val layers: MutableList<List<Int>> = mutableListOf(input)
        while (!layers.last().all { it == 0}) {
            layers += layers.last().zipWithNext().map {(l, r) -> r - l}
        }
        return layers.foldRight(0) { list, acc ->
            list.first() - acc
        }
    }
    fun part1(input: List<String>): Int {
        val sequences = input.map { it.split(" ").map { signedDigits -> signedDigits.toInt() }}
        return sequences.map(::predict).sum()
    }
    fun part2(input: List<String>): Int {
        val sequences = input.map { it.split(" ").map { signedDigits -> signedDigits.toInt() }}
        return sequences.map(::predictFirst).sum() 
    }

    val testInput = readInput("Day09_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 114)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 2)

    val realInput = readInput("Day09")
    part1(realInput).println()
    part2(realInput).println()
}