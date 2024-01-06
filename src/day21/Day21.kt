package day21

import utils.*

typealias Coords = Coords2D

fun getStartCoords(field: List<String>): Coords =
    Coords.iterateOnField(field).firstNotNullOf { coords ->
        coords.takeIf { field[coords.y][coords.x] == 'S' }
    }

fun part1(field: List<String>, count: Int = 64): Int {
    var positions = setOf(getStartCoords(field))
    val size = Coords(field.first().length, field.size)

    repeat(count) {
        positions = buildSet {
            positions.forEach { (x, y) ->
                listOf(
                    Coords(x + 1, y),
                    Coords(x, y + 1),
                    Coords(x - 1, y),
                    Coords(x, y - 1)
                ).forEach { newPosition ->
                    val newY = Math.floorMod(newPosition.y, size.y)
                    val newX = Math.floorMod(newPosition.x, size.x)

                    if (field[newY][newX] != '#')
                        add(newPosition)
                }
            }
        }
    }

    return positions.size
}

fun part2(field: List<String>, count: Int = 26501365): Long {
    val start = getStartCoords(field)
    val size = Coords(field.first().length, field.size)

    check(size.x == size.y)
    check(size.x and 1 == 1)
    check(start == Coords(size.x / 2, size.x / 2))
    check((0..<size.x).all { x -> field[start.y][x] != '#' })
    check((0..<size.y).all { y -> field[y][start.x] != '#' })

    val offset = start.x
    val cycle = size.x

    check((count - offset) % cycle == 0)

    val ys = (0..2).map { x -> part1(field, offset + cycle * x) }

    val c = ys[0]
    val b = 2 * (ys[1] - ys[0]) - (ys[2] - ys[0]) / 2
    val a = ys[1] - ys[0] - b

    val x = 1L * (count - offset) / cycle

    return a * x * x + b * x + c
}

fun main() {
    val testInput = readInput("Day21_test")

    expect(part1(testInput, 3), 6)
    expect(part1(testInput, 6), 16)
    expect(part1(testInput, 10), 50)

    val input = readInput("Day21")

    println(part1(input, 64))
    println(part2(input))
}
