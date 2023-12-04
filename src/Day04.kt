
fun main() {
    data class Game(val number: Int, val winning: List<Int>, val actual: List<Int>)
    fun parseHeader(input: String): Int {
        return input.split(" ").firstNotNullOf(String::toIntOrNull)
    }
    fun parseNumbers(input: String): List<Int> {
        return input.trim().split(" ")
            .filter(String::isNotBlank)
            .map(String::toInt)
    }

    fun parseGame(input: String): Game {
        val splitByColon = input.split(":")
        val gameNumber = parseHeader(splitByColon[0])
        val bodySplitByPipe = splitByColon[1].split("|")
        return Game(gameNumber, parseNumbers(bodySplitByPipe[0]), parseNumbers(bodySplitByPipe[1]))
    }
    fun getScore(game: Game): Int {
        val winners = game.actual.filter { game.winning.contains(it) }
        return winners.fold(0) {
                acc, _ ->
            if (acc == 0) { 1 } else {acc * 2}
        }
    }


    fun part1(input: List<String>): Int {
        return input.map(::parseGame)
            .map(::getScore)
            .sum()
    }

    fun getWinnerCount(game: Game): Int = game.actual.count { game.winning.contains(it) }
    fun List<Int>.addWinnerCounts(thisGame: Int, times: Int, winners: Int): List<Int> =
        this.mapIndexed { i, element ->
            if (i+1 > thisGame && i+1 <= thisGame+winners) {
                element + times
            } else {
                element
            }
    }
    fun part2(input: List<String>): Int {
        val games = input.map(::parseGame)
        val cardCounts = (1..(games.count())).map{1}.toList()
        val totalCounts = games
            .fold(cardCounts) { acc, game -> acc.addWinnerCounts(game.number, acc[game.number - 1], getWinnerCount(game)) }
        return totalCounts.sum()
    }

    val testInput = readInput("Day04_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 13)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 30)

    val realInput = readInput("Day04")
    part1(realInput).println()
    part2(realInput).println()
}