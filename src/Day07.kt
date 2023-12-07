
fun CharSequence.containsExactly(character: Char, count: Int): Boolean {
    return this.count { it == character } == count
}

data class CamelHand(val hand: String, val bid: Int): Comparable<CamelHand> {
    val cardTypesOrdered = listOf(
        'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2',
    )
    fun isFullHouse(): Boolean =
        cardTypesOrdered.count { this.hand.containsExactly(it, 3) } == 1
          && cardTypesOrdered.count {this.hand.containsExactly(it, 2) } == 1
    fun hasNumOfKind(num: Int): Boolean =
        cardTypesOrdered.any { this.hand.containsExactly(it, num) }
    fun isTwoPair(): Boolean =
        cardTypesOrdered.count { this.hand.containsExactly(it, 2)} == 2
    fun isPair(): Boolean =
        cardTypesOrdered.count { this.hand.containsExactly(it, 2)} == 1
    fun getHandRank(): Int {
        return when {
            hasNumOfKind(5) -> 7 // Five of a kind
            hasNumOfKind(4) -> 6 // Four of a kind
            isFullHouse() -> 5
            hasNumOfKind(3) -> 4
            isTwoPair() -> 3
            isPair() -> 2
           else -> 1
        }
    }

    fun compareHighCard(other: CamelHand): Int {
        this.hand.forEachIndexed {
           index, c ->
            if (cardTypesOrdered.indexOf(c) < cardTypesOrdered.indexOf(other.hand[index])) {
               return 1
           } else if (cardTypesOrdered.indexOf(c) > cardTypesOrdered.indexOf(other.hand[index])) {
               return -1
           }
        }
        println("this happened")
        return 0
    }

    override fun compareTo(other: CamelHand): Int {
        return when {
            this.getHandRank() > other.getHandRank() -> 1
            this.getHandRank() < other.getHandRank() -> -1
            this.getHandRank() == other.getHandRank() -> this.compareHighCard(other)
            else -> throw Exception("fudge off")
        }
    }
}
data class JokerHand(val hand: String, val bid: Int): Comparable<JokerHand> {
    val cardTypesOrdered = listOf(
        'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J',
    )
    fun numJokers(): Int = hand.count { it == 'J' }
    fun cardCounts(): List<Int> {
        val counts: MutableList<Int> = cardTypesOrdered.filter{it != 'J'}.map{ c ->
            hand.count { it == c }
        }.sortedDescending().toMutableList()
        counts[0] += numJokers()
        return counts
    }
    fun isFullHouse(): Boolean =
        cardCounts()[0] == 3 && cardCounts()[1] == 2
    fun hasNumOfKind(num: Int): Boolean =
        cardCounts()[0] == num
    fun isTwoPair(): Boolean =
        cardCounts()[0] == 2 && cardCounts()[1] == 2
    fun isPair(): Boolean =
        cardCounts()[0] == 2
    fun getHandRank(): Int {
        return when {
            hasNumOfKind(5) -> 7 // Five of a kind
            hasNumOfKind(4) -> 6 // Four of a kind
            isFullHouse() -> 5
            hasNumOfKind(3) -> 4
            isTwoPair() -> 3
            isPair() -> 2
            else -> 1
        }
    }

    fun compareHighCard(other: JokerHand): Int {
        this.hand.forEachIndexed {
                index, c ->
            if (cardTypesOrdered.indexOf(c) < cardTypesOrdered.indexOf(other.hand[index])) {
                return 1
            } else if (cardTypesOrdered.indexOf(c) > cardTypesOrdered.indexOf(other.hand[index])) {
                return -1
            }
        }
        println("this happened")
        return 0
    }

    override fun compareTo(other: JokerHand): Int {
        return when {
            this.getHandRank() > other.getHandRank() -> 1
            this.getHandRank() < other.getHandRank() -> -1
            this.getHandRank() == other.getHandRank() -> this.compareHighCard(other)
            else -> throw Exception("fudge off")
        }
    }
}

fun main() {
    check(CamelHand("AAAAA", 1).getHandRank() == 7)
    check(CamelHand("AAA2A", 1).getHandRank() == 6)
    check(CamelHand("AAA22", 1).getHandRank() == 5)
    check(CamelHand("AA322", 1).getHandRank() == 3)
    check(CamelHand("25QJQ", 1).getHandRank() == 2)
    fun parseHand(input: String): CamelHand {
        val splitBySpace = input.split(" ")
        return CamelHand(splitBySpace[0], splitBySpace[1].toInt())
    }
    fun parseHand2(input: String): JokerHand {
        val splitBySpace = input.split(" ")
        return JokerHand(splitBySpace[0], splitBySpace[1].toInt())
    }

    fun part1(input: List<String>): Long {
        val hands = input.map(::parseHand)
        val sorted = hands.sorted()
        sorted.println()
        return sorted.foldIndexed(0L) {
            index, acc, camelHand ->
            val score = ((index + 1) * camelHand.bid)
            // println("for ${camelHand.hand} with bid ${camelHand.bid} at index ${index + 1} result was: $score")
            score.toLong() + acc
        }
    }
    fun part2(input: List<String>): Long {
        val hands = input.map(::parseHand2)
        val sorted = hands.sorted()
        sorted.println()
        return sorted.foldIndexed(0L) {
                index, acc, camelHand ->
            val score = ((index + 1) * camelHand.bid)
            // println("for ${camelHand.hand} with bid ${camelHand.bid} at index ${index + 1} result was: $score")
            score.toLong() + acc
        }

    }

    val testInput = readInput("Day07_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 6440L)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 5905L)

    val realInput = readInput("Day07")
    part1(realInput).println()
    part2(realInput).println()
}