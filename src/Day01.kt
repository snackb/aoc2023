fun main() {
    fun part1(input: List<String>): Int {
        return input.map { str: String ->
            str.first { chr -> chr.isDigit() }.toString() +  str.last { chr -> chr.isDigit() }.toString()
        }.sumOf { str -> str.toInt() }
    }

    val numbers = listOf(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine"
    )

    fun String.valueFirstDigit(): Char {
        val firstActualDigit = this.indexOfFirst { chr -> chr.isDigit() }
        val firstSpeltDigit = this.findAnyOf(numbers)
        return if ( firstSpeltDigit == null || firstActualDigit < firstSpeltDigit.first && firstActualDigit != -1) {
            this[firstActualDigit]
        } else {
            (numbers.indexOf(firstSpeltDigit.second) + 1).toString()[0]
        }
    }

    fun String.valueLastDigit(): Char {
        val lastActualDigit = this.indexOfLast { chr -> chr.isDigit() }
        val lastSpeltDigitPair = this.findLastAnyOf(numbers)
        return if ( lastSpeltDigitPair == null || lastActualDigit > lastSpeltDigitPair.first){
            this[lastActualDigit]
        } else {
            (numbers.indexOf(lastSpeltDigitPair.second) + 1).toString()[0]
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf{ line ->
           val firstDigit = line.valueFirstDigit().toString()
           val lastDigit = line.valueLastDigit().toString()
            (firstDigit + lastDigit).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val test2Input = readInput("Day01_02_test")

    check(part1(testInput) == 142)
    println(part2(test2Input))

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
