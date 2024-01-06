package day18

import utils.*
import kotlin.math.absoluteValue

typealias Coords = Coords2D

data class Line(val origin: Coords, val direction: Coords, val length: Int) {
    val tip: Coords = origin + direction * length
}

fun parseInput1(input: List<String>): List<Line> {
    var origin = Coords(0, 0)

    return input.map { line ->
        val (c, length, _) = line.split(" ")
        val direction = when (c.single()) {
            'R' -> Coords(1, 0)
            'D' -> Coords(0, 1)
            'L' -> Coords(-1, 0)
            'U' -> Coords(0, -1)
            else -> throw Error("invalid direction $c")
        }

        Line(origin, direction, length.toInt()).also { origin = it.tip }
    }
}

fun parseInput2(input: List<String>): List<Line> {
    var origin = Coords(0, 0)

    return input.map { line ->
        val (_, _, color) = line.split(" ")
        val direction = when (color[7]) {
            '0' -> Coords(1, 0)
            '1' -> Coords(0, 1)
            '2' -> Coords(-1, 0)
            '3' -> Coords(0, -1)
            else -> throw Error("invalid direction ${color[7]}")
        }

        Line(
            origin,
            direction,
            color.substring(2..6).toInt(16)
        ).also { origin = it.tip }
    }
}

fun solve(lines: List<Line>) : Long {
    val coords = lines.map { it.origin }
    val area = (coords + coords.first()).windowed(2).sumOf { (a, b) ->
        1L * a.y * b.x - 1L * a.x * b.y
    }.absoluteValue / 2

    return area + lines.sumOf { 1L * it.length } / 2 + 1
}

fun part1(input: List<String>): Long = solve(parseInput1(input))

fun part2(input: List<String>): Long = solve(parseInput2(input))

fun main() {
    val testInput = readInput("Day18_test")

    expect(part1(testInput), 62)
    expect(part2(testInput), 952408144115)

    val input = readInput("Day18")

    println(part1(input))
    println(part2(input))
}
