data class Game(val number: Int, val draws: List<Map<Colour, Int>>)

enum class Colour {
    RED,
    GREEN,
    BLUE,
}

fun parseHeaderToGameNumber(header: String): Int = header.split(" ")[1].toInt()

fun String.parseColour(): Colour {
    return Colour.valueOf(this.trim().uppercase())
}

fun parsePair(input: String): Pair<Colour, Int> {
    val numberAndColour = input.trim().split(" ")
    val number = numberAndColour[0].toInt()
    val colour = numberAndColour[1].parseColour()
    return colour to number
}

fun parseBodyToDraws(body: String): List<Map<Colour, Int>> {
    val splitBySemicolons = body.split(";")
    val splitBySemicolonsThenCommas = splitBySemicolons.map { it.split(",")}
    return splitBySemicolonsThenCommas.map {
        drawSplitByCommas ->
        drawSplitByCommas.map(::parsePair)
    }.map {it.toMap()}
}

fun String.parseGame(): Game {
    val splitByHeader = this.split(":")
    val gameNumber = parseHeaderToGameNumber(splitByHeader[0])
    val draws = parseBodyToDraws(splitByHeader[1])
    return Game(gameNumber, draws)
}

fun validityPredicateForCount(red: Int, green: Int, blue: Int): (Game) -> Boolean {
    return {
        game ->
        game.draws.all {
            it.getOrDefault(Colour.BLUE, 0) <= blue
                    && it.getOrDefault(Colour.RED, 0) <= red
                    && it.getOrDefault(Colour.GREEN, 0) <= green
        }
    }
}



fun minNeededToWin(game: Game): Map<Colour, Int> {
    val minimum = mutableMapOf(
            Colour.BLUE to 0,
            Colour.RED to 0,
            Colour.GREEN to 0)
    for (draw in game.draws) {
        for (colour in Colour.entries) {
            if (draw.getOrDefault(colour, 0) > minimum[colour]!!) {
                minimum[colour] = draw[colour]!!
            }
        }
    }
    return minimum
}

fun powerOf(map: Map<Colour, Int>): Int =
        Colour.entries.map { map.getOrDefault(it, 0) }
            .reduce(Int::times)


fun main() {
    fun part1(input: List<String>): Int {
        val games = input
                .map(String::parseGame)
        val validGames = games
                .filter(validityPredicateForCount(red = 12, green = 13, blue = 14))
        return validGames.sumOf { it.number }
    }
    fun part2(input: List<String>): Int{
        return input.map(String::parseGame)
                .map(::minNeededToWin)
                .sumOf(::powerOf)
    }
    val testResult = part1(readInput("Day02_test"))
    testResult.println()
    check(testResult == 8)
    val testResult2 = part2(readInput("Day02_test"))
    testResult2.println()
    check(testResult2 == 2286)
    val realInput = readInput("Day02")
    part1(realInput).println()
    part2(realInput).println()
}