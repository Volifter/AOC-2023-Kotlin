package day12

import utils.*

fun parseInput(input: List<String>): List<Pair<String, List<Int>>> =
    input.map { line ->
        val (seq, groups) = line.split(" ")

        Pair(seq, groups.split(",").map(String::toInt))
    }

fun solve(
    seq: String,
    groups: List<Int>,
    cache: MutableMap<Pair<String, List<Int>>, Long> = mutableMapOf()
): Long = cache.getOrPut(Pair(seq, groups)) {
    if (seq.isEmpty())
        return@getOrPut if (groups.isEmpty()) 1 else 0

    if (groups.isEmpty())
        return@getOrPut if (seq.all { it != '#' }) 1 else 0

    return@getOrPut when (seq[0]) {
        '.' -> solve(seq.dropWhile { it == '.' }, groups, cache)
        '?' -> (
            solve('.' + seq.substring(1), groups, cache)
            + solve('#' + seq.substring(1), groups, cache)
        )
        else -> {
            val groupSize = groups.first()

            if (
                seq.length >= groupSize
                && seq.substring(0..<groupSize).all { it != '.' }
                && seq.getOrNull(groupSize) != '#'
            )
                solve(
                    seq.substring(minOf(groupSize + 1, seq.length)),
                    groups.drop(1),
                    cache
                )
            else
                0
        }
    }
}

fun part1(input: List<String>): Long =
    parseInput(input).sumOf { (seq, groups) -> solve(seq, groups) }

fun part2(input: List<String>): Long =
    parseInput(input).sumOf { (seq, groups) -> solve(
        List(5) { seq }.joinToString("?"),
        List(5) { groups }.flatten()
    ) }

fun main() {
    val testInput = readInput("Day12_test")

    expect(part1(testInput), 21)
    expect(part2(testInput), 525152)

    val input = readInput("Day12")

    println(part1(input))
    println(part2(input))
}
