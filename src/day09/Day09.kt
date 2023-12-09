package day09

import utils.*

fun parseInput(input: List<String>): List<List<Int>> =
    input.map { it.split(" ").map(String::toInt) }

fun extrapolateValue(values: List<Int>, direction: Int) : Int {
    val steps = generateSequence(values) { step ->
        if (step.all { it == 0 })
            null
        else
            step.zipWithNext { a, b -> b - a }
    }.toList().reversed().drop(1).iterator()

    return generateSequence(0) { prev ->
        if (!steps.hasNext())
            return@generateSequence null

        val step = steps.next()

        if (direction > 0)
            step.last() + prev
        else
            step.first() - prev
    }.last()
}

fun part1(input: List<String>): Int =
    parseInput(input).sumOf { extrapolateValue(it, 1) }

fun part2(input: List<String>): Int =
    parseInput(input).sumOf { extrapolateValue(it, -1) }

fun main() {
    val testInput = readInput("Day09_test")

    expect(part1(testInput), 114)
    expect(part2(testInput), 2)

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}
