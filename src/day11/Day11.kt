package day11

import utils.*

typealias Coords = Coords2D

fun parseInput(input: List<String>): Pair<List<Coords>, Coords> =
    Pair(
        Coords.iterateOnField(input).mapNotNull {
            it.takeIf { (x, y) -> input[y][x] == '#' }
        }.toList(),
        Coords(input.first().length, input.size)
    )

fun expandUniverse(
    galaxies: List<Coords>,
    size: Coords,
    factor: Int
): List<Coords2D> {
    val emptyCols = (
        (0..size.x).toSet() - galaxies.map { it.x }.toSet()
    ).sorted()
    val emptyRows = (
        (0..size.y).toSet() - galaxies.map { it.y }.toSet()
    ).sorted()

    return galaxies.map { coords ->
        coords + Coords(
            emptyCols.indexOfFirst { it > coords.x } * (factor - 1),
            emptyRows.indexOfFirst { it > coords.y } * (factor - 1)
        )
    }
}

fun solve(input: List<String>, factor: Int = 1): Long {
    val (galaxies, size) = parseInput(input)
    val expanded = expandUniverse(galaxies, size, factor).toList()

    return expanded.withIndex().sumOf { (i, a) ->
        expanded.drop(i).sumOf { b ->
            (a - b).manhattanDelta.toLong()
        }
    }
}

fun main() {
    val testInput = readInput("Day11_test")

    expect(solve(testInput, 2), 374)
    expect(solve(testInput, 10), 1030)
    expect(solve(testInput, 100), 8410)

    val input = readInput("Day11")

    println(solve(input, 2))
    println(solve(input, 1000000))
}
