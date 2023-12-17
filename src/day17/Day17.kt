package day17

import utils.*
import java.util.PriorityQueue

typealias Coords = Coords2D

data class Path(
    val position: Coords,
    val direction: Coords,
    val lineLength: Int
) {
    fun getNextPaths(lineRange: IntRange) = sequence {
        if (lineLength < lineRange.last)
            yield(Path(position + direction, direction, lineLength + 1))

        if (lineLength >= lineRange.first) {
            yieldAll(
                sequenceOf(
                    Coords(-direction.y, direction.x),
                    Coords(direction.y, -direction.x)
                ).map { Path(position + it, it, 1) }
            )
        }
    }
}

fun parseInput(input: List<String>): List<List<Int>> =
    input.map { line -> line.map { it.digitToInt() } }

fun solve(field: List<List<Int>>, lineRange: IntRange = (1..3)): Int {
    val size = Coords(field.first().size, field.size)
    val target = size - Coords(1, 1)
    val queue = PriorityQueue<Pair<Int, Path>>(2) { (a, _), (b, _) -> a - b }

    queue.addAll(listOf(
        Pair(0, Path(Coords(0, 0), Coords(1, 0), 0)),
        Pair(0, Path(Coords(0, 0), Coords(0, 1), 0))
    ))

    val visited = queue.map { it.second }.toMutableSet()

    while (queue.isNotEmpty()) {
        val (cost, path) = queue.poll()

        if (path.position == target)
            return cost

        queue.addAll(
            path.getNextPaths(lineRange)
                .filter { it.position in size && visited.add(it) }
                .map { nextPath ->
                    Pair(
                        cost + field[nextPath.position.y][nextPath.position.x],
                        nextPath
                    )
                }
        )
    }

    throw IllegalStateException("no path found")
}

fun part1(input: List<String>): Int = solve(parseInput(input))

fun part2(input: List<String>): Int = solve(parseInput(input), (4..10))

fun main() {
    val testInput = readInput("Day17_test")

    expect(part1(testInput), 102)
    expect(part2(testInput), 94)

    val input = readInput("Day17")

    println(part1(input))
    println(part2(input))
}
