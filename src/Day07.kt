
fun CharSequence.containsExactly(character: Char, count: Int): Boolean {
    return this.count { it == character } == count
}

data class CamelHand(val hand: String, val bid: Int): Comparable<CamelHand> {
    val cardTypesOrdered = listOf(
        'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2',
    )
    fun isFullHouse(): Boolean =
        cardTypesOrdered.any { this.hand.containsExactly(it, 3) }
          && cardTypesOrdered.any {this.hand.containsExactly(it, 2) }
    fun hasNumOfKind(num: Int): Boolean =
        cardTypesOrdered.any { this.hand.containsExactly(it, num) }
    fun isTwoPair(): Boolean =
        cardTypesOrdered.count { this.hand.containsExactly(it, 2)} == 2
    fun getHandRank(): Int {
        return when {
            hasNumOfKind(5) -> 7 // Five of a kind
            hasNumOfKind(4) -> 6 // Four of a kind
            isFullHouse() -> 5
            hasNumOfKind(3) -> 4
            isTwoPair() -> 3

           else -> TODO()
        }
    }
    override fun compareTo(other: CamelHand): Int {

        TODO("Not yet implemented")
    }
}

fun main() {
    check(CamelHand("AAAAA", 1).getHandRank() == 7)
    check(CamelHand("AAA2A", 1).getHandRank() == 6)
    check(CamelHand("AAA22", 1).getHandRank() == 5)
    check(CamelHand("AA322", 1).getHandRank() == 3)
    fun parseHand(input: String): CamelHand {
        val splitBySpace = input.split(" ")
        return CamelHand(splitBySpace[0], splitBySpace[1].toInt())
    }

    fun part1(input: List<String>): Int {
        val hands = input.map(::parseHand)

        return 6440
    }
    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day07_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 6440)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 0)

    val realInput = readInput("Day07")
    part1(realInput).println()
    part2(realInput).println()
}