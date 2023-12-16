package day16

import utils.*

typealias Coords = Coords2D

data class Beam(val position: Coords, val direction: Coords) {
    val advanced get() = Beam(position + direction, direction)
}

fun solve(
    field: List<String>,
    startBeam: Beam = Beam(Coords2D(0, 0), Coords(1, 0))
): Int {
    val size = Coords(field.first().length, field.size)
    var beams = listOf(startBeam)
    val visited = mutableSetOf(startBeam)

    while (beams.isNotEmpty()) {
        beams = sequence {
            beams.forEach { beam ->
                val c = field[beam.position.y][beam.position.x]

                when {
                    c == '\\' -> yield(
                        Beam(
                            beam.position,
                            Coords(beam.direction.y, beam.direction.x)
                        )
                    )
                    c == '/' -> yield(
                        Beam(
                            beam.position,
                            Coords(-beam.direction.y, -beam.direction.x)
                        )
                    )
                    c == '|' && beam.direction.x != 0 -> {
                        yield(Beam(beam.position, Coords(0, -1)))
                        yield(Beam(beam.position, Coords(0, 1)))
                    }
                    c == '-' && beam.direction.y != 0 -> {
                        yield(Beam(beam.position, Coords(-1, 0)))
                        yield(Beam(beam.position, Coords(1, 0)))
                    }
                    else -> yield(beam)
                }
            }
        }
            .map { it.advanced }
            .filter { it.position in size && it !in visited }
            .onEach { visited.add(it) }
            .toList()
    }

    return visited.map { it.position }.toSet().size
}

fun part1(input: List<String>): Int = solve(input)

fun getBorderBeams(size: Coords): List<Beam> = (
    (0..<size.y).flatMap { y ->
        listOf(
            Beam(Coords(0, y), Coords(1, 0)),
            Beam(Coords(size.x - 1, y), Coords(-1, 0))
        )
    }
    + (0..<size.x).flatMap { x ->
        listOf(
            Beam(Coords(x, 0), Coords(0, 1)),
            Beam(Coords(x, size.y - 1), Coords(0, -1))
        )
    }
)

fun part2(input: List<String>): Int =
    getBorderBeams(Coords(input.first().length, input.size))
        .maxOf { solve(input, it) }

fun main() {
    val testInput = readInput("Day16_test")

    expect(part1(testInput), 46)
    expect(part2(testInput), 51)

    val input = readInput("Day16")

    println(part1(input))
    println(part2(input))
}
