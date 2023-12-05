package day05

import utils.*

data class Mapping(val range: LongRange, val offset: Long) {
    fun splitRange(other: LongRange): List<LongRange?> = listOf(
        (other.first..<range.first),
        (
            maxOf(range.first, other.first) + offset
            ..minOf(range.last, other.last) + offset
        ),
        (range.last + 1..other.last)
    ).map { it.takeUnless { it.isEmpty() } }
}

fun parseInput(input: List<String>): Pair<List<Long>, List<List<Mapping>>> {
    val groups = groupLines(input)
    val initial = groups[0][0]
        .removePrefix("seeds: ")
        .split(" ")
        .map(String::toLong)
    val ranges = groups.drop(1).map { group ->
        group
            .drop(1)
            .map {
                val (destination, origin, count) = it
                    .split(" ")
                    .map(String::toLong)

                Mapping((origin..<origin + count), destination - origin)
            }
            .sortedBy { it.range.first }
    }

    return Pair(initial, ranges)
}

fun part1(input: List<String>): Long {
    val (initialValues, steps) = parseInput(input)
    val step = steps.iterator()

    return generateSequence(initialValues) { values ->
        if (!step.hasNext())
            return@generateSequence null

        val mappings = step.next()

        values.map { value ->
            mappings
                .find { value in it.range }
                ?.let { value + it.offset }
                ?: value
        }
    }.last().min()
}

fun part2(input: List<String>): Long {
    val (entries, steps) = parseInput(input)
    val initialRanges = entries
        .chunked(2)
        .map { (a, b) -> (a..<a + b) }
        .sortedBy { it.first }
    val step = steps.iterator()
    val ranges = generateSequence(initialRanges) { rangesList ->
        if (!step.hasNext())
            return@generateSequence null

        buildList {
            val mappings = ArrayDeque(step.next())
            val ranges = ArrayDeque(rangesList)

            while (ranges.isNotEmpty() && mappings.isNotEmpty()) {
                val nextRange = ranges.first()
                val nextMapping = mappings.first()

                when {
                    nextMapping.range.last < nextRange.first ->
                        mappings.removeFirst()
                    nextMapping.range.first > nextRange.last ->
                        add(ranges.removeFirst())
                    else -> {
                        val (left, mid, right) = nextMapping.splitRange(
                            ranges.removeFirst()
                        )

                        left?.let { add(it) }
                        add(mid!!)
                        right?.let { ranges.addFirst(it) }
                    }
                }
            }

            addAll(ranges)
        }.sortedBy { it.first }
    }.last()

    return ranges.first().first
}

fun main() {
    val testInput = readInput("Day05_test")

    expect(part1(testInput), 35)
    expect(part2(testInput), 46)

    val input = readInput("Day05")

    println(part1(input))
    println(part2(input))
}
