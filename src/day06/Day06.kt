package day06

import utils.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun parseInputForPart1(input: List<String>): List<Pair<Long, Long>> = input
    .map { line ->
        line.split(" ").filterNot(String::isEmpty).drop(1).map(String::toLong)
    }
    .let { (times, distances) -> times.zip(distances) }

fun parseInputForPart2(input: List<String>): List<Pair<Long, Long>> = listOf(
    input
        .map { it.substringAfter(":").replace(" ", "").toLong() }
        .let { (a, b) -> Pair(a, b) }
)

fun solve(races: List<Pair<Long, Long>>): Long = races
    .map { (time, distance) ->
        val d = sqrt((time * time - 4 * distance) * 1.0)
        val from = (time - d) / 2
        val to = (time + d) / 2

        (ceil(to) - floor(from) - 1).toLong()
    }
    .reduce(Long::times)

fun part1(input: List<String>): Long = solve(parseInputForPart1(input))

fun part2(input: List<String>): Long = solve(parseInputForPart2(input))

fun main() {
    val testInput = readInput("Day06_test")

    expect(part1(testInput), 288)
    expect(part2(testInput), 71503)

    val input = readInput("Day06")

    println(part1(input))
    println(part2(input))
}
