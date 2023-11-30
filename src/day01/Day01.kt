package day01

import utils.*

fun part1(input: List<String>): Int = input.first().toInt()

fun part2(input: List<String>): Int = input.first().toInt() + 1

fun main() {
    val testInput = readInput("Day01_test")

    expect(part1(testInput), 2023)
    expect(part2(testInput), 2024)

    // val input = readInput("Day01")
    //
    // println(part1(input))
    // println(part2(input))
}
