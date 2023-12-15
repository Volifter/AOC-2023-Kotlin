package day15

import utils.*

data class Lens(val box: Int, val focalLength: Int)

fun parseInput(input: String): List<Pair<String, Int?>> =
    input.split(",").map { line ->
        val (name, value) = line.split("=", "-")

        Pair(name, value.takeIf { it.isNotEmpty() }?.toInt())
    }

fun hash(str: String): Int =
    str.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }

fun part1(input: List<String>): Int =
    input.first().split(",").sumOf { hash(it) }

fun part2(input: List<String>): Int {
    val lines = parseInput(input.first())
    val lenses = mutableMapOf<String, Lens>()

    lines.forEach { (name, number) ->
        number
            ?.let { lenses[name] = Lens(hash(name), it) }
            ?: lenses.remove(name)
    }

    return lenses.values.groupBy { it.box }.values.sumOf { box ->
        box.withIndex().sumOf { (i, lens) ->
            (lens.box + 1) * (i + 1) * lens.focalLength
        }
    }
}

fun main() {
    val testInput = readInput("Day15_test")

    expect(part1(testInput), 1320)
    expect(part2(testInput), 145)

    val input = readInput("Day15")

    println(part1(input))
    println(part2(input))
}
