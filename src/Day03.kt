typealias Coord = Pair<Int, Int>
typealias Symbols = MutableMap<Coord, Char>
typealias Numbers = MutableMap<Coord, String>

fun consumeNumber(input: String): String {
    val end = input.indexOfFirst { !it.isDigit() }
    return if (end > 0) {
        input.take(end)
    } else {
        input
    }

}
fun parseDiagram(input: List<String>): Pair<Symbols, Numbers> {
    val symbols: Symbols = mutableMapOf()
    val numbers: Numbers = mutableMapOf()
    for ((y, line) in input.withIndex()){
        var x = 0
        while (x < line.length) {
           if (line[x].isDigit()) {
               val number = consumeNumber(line.substring(x))
               numbers[x to y] = number
               x += number.length
           } else if (line[x] != '.') {
               symbols[x to y] = line[x]
               x += 1
           } else {
               x += 1
           }
        }
    }
    return symbols to numbers
}

fun Map.Entry<Coord, String>.isContainedInSymbols(symbols: Symbols): Boolean {
    val coord = this.key
    val startx = coord.first - 1
    val starty = coord.second - 1
    for (x in startx..(coord.first + this.value.length)) {
        for (y in starty..starty+2) {
            if (symbols.containsKey(x to y)) {
                return true
            }
        }
    }
    return false
}

typealias GearCandidates = Map<Coord, Pair<Int, Int>>

fun Map.Entry<Coord, String>.getAttachedGears(symbols: Symbols): Sequence<Coord> {
    val coord = this.key
    val number = this.value
    val startx = coord.first - 1
    val starty = coord.second - 1
    return sequence {
        for (x in startx..(coord.first + number.length)) {
            for (y in starty..starty+2) {
                if (symbols[x to y] == '*') {
                    yield(x to y)
                }
            }
        }
    }
}

fun determineGears(symbols: Symbols, numbers: Numbers): GearCandidates {
    val gears = numbers.map { it.value to it.getAttachedGears(symbols)}
            .flatMap { (number, gears) -> gears.map { gear -> gear to number  }}
            .groupBy({ (gear, _) -> gear }, { (_, number) -> number })
            .filter {it.value.size == 2}
            .mapValues { (_, numbers) -> numbers[0].toInt() to numbers[1].toInt() }
    return gears
}

fun main() {
    fun part1(input: List<String>): Int {
        val (symbols, numbers) = parseDiagram(input)
        val filtered = numbers.filter {
            it.isContainedInSymbols(symbols)
        }
        val sum = filtered.asIterable()
                .sumOf { it.value.toInt() }
        return sum
    }

    fun part2(input: List<String>): Int{
        val (symbols, numbers) = parseDiagram(input)
        val gears = determineGears(symbols, numbers)
        val ratios = gears.map { it.value.first * it.value.second}
        return ratios.sum()
    }

    val testInput = readInput("Day03_test")

    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 4361)

    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 467835)

    val actualInput = readInput("Day03")
    part1(actualInput).println()
    part2(actualInput).println()
}
