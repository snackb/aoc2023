import kotlin.math.max
import kotlin.math.min

data class SeedRange(val min: Long, val max: Long)
data class RangePair(val dest: Long, val source: Long, val length: Long)
data class RangeMap(val sourceName: String, val destName: String, val rangePairs: List<RangePair>)
fun main() {
    fun parseHeader(input: String): List<Long>{
        val splitByColon = input.splitToSequence(":")
        val splitBySpaces = splitByColon.drop(1).first().split(" ")
        return splitBySpaces.mapNotNull(String::toLongOrNull)
    }
    fun parseRangePair(input: String): RangePair {
        val numbers = input.trim().split(" ").map { it.trim().toLong() }
        val destStart = numbers[0]
        val sourceStart = numbers[1]
        val length = numbers[2]
        return RangePair(destStart, sourceStart, length)
    }
    fun parseRangeMap(input: String): RangeMap {
        fun parseMapHeader(input: String): Pair<String, String> {
            val splitBySpace = input.split(" ")
            val names = splitBySpace[0].split("-to-")
            return names[0] to names[1]
        }
        val lines = input.trim().split("\n")
        val (source, dest) = parseMapHeader(lines[0])
        val rangePairs = lines.drop(1).map(::parseRangePair)
        return RangeMap(source, dest, rangePairs)
    }
    fun parseRangeMaps(input: List<String>): List<RangeMap> {
        val paragraphs = input.joinToString("\n").split("\n\n")
        return paragraphs.map(::parseRangeMap)
    }
    fun Long.applyMap(map: RangeMap): Long {
        val match =
            map.rangePairs.singleOrNull { this >= it.source && this < it.source + it.length }
        return if (match != null) {
            match.dest + (this - match.source)
        } else {
            this
        }

    }

    fun part1(input: List<String>): Long {
        val seeds = parseHeader(input[0])
        val rangeMaps = parseRangeMaps(input.drop(1))
        val locations = rangeMaps.fold(seeds) { acc, rangeMap -> acc.map { it.applyMap(rangeMap) } }
        return locations.min()
    }
    fun getSeedRanges(input: List<Long>): List<SeedRange> {
        val pairs = input.zipWithNext().filterIndexed { i, _ -> i%2==0 }
        return pairs.map { SeedRange(it.first, it.first + it.second - 1)}
    }
    fun SeedRange.applyMapWhenIntersects(map: RangePair): SeedRange? {
        val intersectionMin = max(map.source, this.min)
        val intersectionMax = min(map.source + map.length - 1, this.max)
        val mappedIntersectionMin = map.dest + (intersectionMin - map.source)
        val mappedIntersectionMax = map.dest + (intersectionMax - map.source)
        return if (intersectionMax >= intersectionMin) SeedRange(
            mappedIntersectionMin,
            mappedIntersectionMax
        )
        else return null
    }

    fun SeedRange.subtract(map: RangePair): Sequence<SeedRange> {
        val mapee = this
        return sequence {
            val mapTop = map.source + map.length - 1
            if (mapee.min < map.source) {
                yield(SeedRange(mapee.min, min(mapee.max, map.source - 1)))
            }
            if (mapee.max > mapTop) {

                yield(SeedRange(max(mapTop, mapee.min), mapee.max))
            }
        }
    }

    fun part2(input: List<String>): Long {
        val seeds = parseHeader(input[0])
        val seedRanges = getSeedRanges(seeds)
        seedRanges.println()
        val rangeMaps = parseRangeMaps(input.drop(1))
        val mappedRanges = rangeMaps.fold(seedRanges) {
            seedRangesForMap, map ->
            val mappedIntersections = map.rangePairs.flatMap {
                    pair ->
                seedRangesForMap.mapNotNull { it.applyMapWhenIntersects(pair) }
            }
            val subtractedRanges = map.rangePairs.fold(seedRangesForMap) {
                seedRangesForPair, pair ->
                seedRangesForPair.flatMap { it.subtract(pair) }
            }
            mappedIntersections+subtractedRanges
        }
        return mappedRanges.minOf { it.min }
    }

    val testInput = readInput("Day05_test")
    val testResult1 = part1(testInput)
    testResult1.println()
    check(testResult1 == 35L)
    val testResult2 = part2(testInput)
    testResult2.println()
    check(testResult2 == 46L)

    val realInput = readInput("Day05")
    part1(realInput).println()
    part2(realInput).println()
}