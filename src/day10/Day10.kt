package day10

import utils.*
import kotlin.math.absoluteValue

typealias Coords = Coords2D

data class Pipe(val position: Coords, val connected: Set<Coords>) {
    constructor(position: Coords, c: Char) : this(
        position,
        when (c) {
            '-' -> setOf(position + Coords(-1, 0), position + Coords(1, 0))
            '|' -> setOf(position + Coords(0, -1), position + Coords(0, 1))
            'L' -> setOf(position + Coords(1, 0), position + Coords(0, -1))
            'J' -> setOf(position + Coords(-1, 0), position + Coords(0, -1))
            '7' -> setOf(position + Coords(-1, 0), position + Coords(0, 1))
            'F' -> setOf(position + Coords(1, 0), position + Coords(0, 1))
            else -> throw Error("Invalid pipe char $c")
        }
    )

    val isConnectedDownwards get() =
        connected.any { it.y == position.y + 1 }
}

fun parseInput(input: List<String>): Pair<Map<Coords, Pipe>, Coords> {
    var start: Coords? = null
    val pipes = mutableMapOf<Coords, Pipe>()

    Coords.iterate(Coords(input.first().length, input.size)).forEach { coords ->
        when (val c = input[coords.y][coords.x]) {
            'S' -> { start = coords }
            '.' -> {}
            else -> pipes[coords] = Pipe(coords, c)
        }
    }

    return Pair(pipes, start!!)
}

fun getLoop(
    pipes: Map<Coords, Pipe>,
    start: Coords,
    dir: Coords
): List<Coords>? = buildList {
    var prevPos = start
    var pipe = pipes[start + dir] ?: return null

    add(start)

    while (prevPos in pipe.connected) {
        add(pipe.position)

        val nextPos = (pipe.connected - prevPos).single()

        prevPos = pipe.position
        pipe = pipes[nextPos] ?: return@buildList
    }
}.takeIf { (it.last() - start).manhattanDelta.absoluteValue == 1 }

fun getLoopFromStart(pipes: Map<Coords, Pipe>, start: Coords): List<Coords> =
    listOf(Coords(-1, 0), Coords(1, 0), Coords(0, -1), Coords(0, 1))
        .asSequence()
        .mapNotNull { getLoop(pipes, start, it) }
        .first()

fun part1(input: List<String>): Int {
    val (pipes, start) = parseInput(input)

    return getLoopFromStart(pipes, start).size / 2
}

fun getLoopPipes(loop: List<Coords>): Map<Coords, Pipe> =
    (listOf<Coords>() + loop.last() + loop + loop.first())
        .windowed(3)
        .associate { (prev, coords, next) ->
            coords to Pipe(coords, setOf(prev, next))
        }

fun part2(input: List<String>): Int {
    val (pipes, start) = parseInput(input)
    val loopPipes = getLoopPipes(getLoopFromStart(pipes, start))
    var isInside = false

    return Coords.iterateOnField(input).count { coords ->
        loopPipes[coords]
            ?.let {
                if (it.isConnectedDownwards)
                    isInside = !isInside

                false
            }
            ?: isInside
    }
}

fun main() {
    expect(part1(readInput("Day10_test_a")), 4)
    expect(part1(readInput("Day10_test_b")), 8)
    expect(part2(readInput("Day10_test_c")), 8)
    expect(part2(readInput("Day10_test_d")), 10)

    val input = readInput("Day10")

    println(part1(input))
    println(part2(input))
}
