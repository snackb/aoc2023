
inline fun <T> Sequence<T>.countLong(predicate: (T) -> Boolean): Long{
    var count = 0L
    for (element in this) if (predicate(element)) ++count
    return count
}
fun main() {
    data class SpringRecord(val springs: List<Char>, val runCounts: List<Int>)

    fun parseSpringRecord(input: String): SpringRecord {
        val splitBySpace = input.split(" ")
        val springs = splitBySpace[0].toList()
        val runCounts = splitBySpace[1].split(",").map(String::toInt)
        return SpringRecord(springs, runCounts)
    }


    fun countMatchingPerms(unknownSprings: List<Char>, predicate: (CharArray) -> Boolean): Long {
        return sequence<CharArray> {
            val unknowns = unknownSprings.count { it == '?'}
            val numPerms = 1L shl unknowns
            numPerms.println()
            val arr = unknownSprings.toCharArray()
            for (i in 0..<numPerms) {
                var j = i
                for (x in arr.indices) {
                    if (unknownSprings[x] == '?') {
                        arr[x] = if (j % 2 == 0L) '.' else '#'
                        j = j shr 1
                    }
                }
                yield(arr.clone())
            }
        }.countLong(predicate)
    }

    fun SpringRecord.getValidPermutations(): Long {
        fun isValid(arr: CharArray): Boolean {
            var inRun: Boolean = false
            var runStart = 0
            val countsList: MutableList<Int> = mutableListOf()
            for (i in arr.indices) {
               when (arr[i]) {
                   '#' -> {
                       if (!inRun) {
                           inRun = true
                           runStart = i
                       }
                   }
                   '.' -> if (inRun) {
                       inRun = false
                       countsList.add(i-runStart)
                   }
                   else -> error("buh")
               }
            }
            if (inRun) {
                countsList.add(arr.count() - runStart)
            }
            return countsList == runCounts
        }
        return countMatchingPerms(springs, ::isValid)
    }

    fun part1(input: List<String>): Long {
        val records = input.map(::parseSpringRecord)
        records.println()
        parseSpringRecord("???.### 1,1,3").getValidPermutations().println()
        val valid = records.map { it to it.getValidPermutations()}
        valid.println()
        return valid.sumOf { it.second }
    }
    fun parseSpringRecord2(input: String): SpringRecord {
        val splitBySpace = input.split(" ")
        val springs = splitBySpace[0].repeat(5).toList()
        val runCounts = splitBySpace[1].split(",").map(String::toInt)
        return SpringRecord(springs, (1..5).flatMap{runCounts})
    }
    fun part2(input: List<String>): Long {
        val records = input.map(::parseSpringRecord2)
        records.println()
        parseSpringRecord2("???.### 1,1,3").getValidPermutations().println()
        val valid = records.map { it to it.getValidPermutations()}
        valid.println()
        return valid.sumOf { it.second }
    }

    val testInput = readInput("Day12_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 21L)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 525152L)

    val realInput = readInput("Day12")
    part1(realInput).println()
    part2(realInput).println()
}