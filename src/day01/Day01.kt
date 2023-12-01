package day01

import utils.*

fun findEdgeDigits(string: String, dict: Map<String, Int>): Pair<Int, Int> {
    val occurrences = dict.flatMap { (s, n) -> listOf(
        Pair(n, string.indexOf(s)),
        Pair(n, string.lastIndexOf(s))
    ) }.filter { it.second != -1 }
    val first = occurrences.minBy { it.second }.first
    val last = occurrences.maxBy { it.second }.first

    return Pair(first, last)
}

fun part1(input: List<String>): Int {
    val dict = (0..9).associateBy(Int::toString)

    return input.sumOf { line ->
        findEdgeDigits(line, dict).let { it.first * 10 + it.second }
    }
}

fun part2(input: List<String>): Int {
    val dict = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    ) + (0..9).associateBy(Int::toString)

    return input.sumOf { line ->
        findEdgeDigits(line, dict).let { it.first * 10 + it.second }
    }
}

fun main() {
    expect(part1(readInput("Day01_test_a")), 142)
    expect(part2(readInput("Day01_test_b")), 281)

    val input = readInput("Day01")

    println(part1(input))
    println(part2(input))
}
