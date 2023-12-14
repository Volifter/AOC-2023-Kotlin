package day14

import utils.*

typealias Coords = Coords2D

typealias Field = List<List<Char>>

fun slideUp(field: Field): Field {
    val size = Coords(field.first().size, field.size)
    val targets = MutableList(size.x) { 0 }
    val result = field.map { line -> line.toMutableList() }

    Coords.iterate(size).forEach { (x, y) ->
        when (result[y][x]) {
            'O' -> {
                result[y][x] = '.'
                result[targets[x]++][x] = 'O'
            }
            '#' -> {
                targets[x] = y + 1
            }
        }
    }

    return result
}

fun getFieldScore(field: Field): Int =
    field.withIndex().sumOf { (i, line) ->
        line.count { c -> c == 'O' } * (field.size - i)
    }

fun part1(input: List<String>): Int =
    getFieldScore(slideUp(input.map { it.toList() }))

fun rotatedCW(field: Field): Field =
    field.first().indices.map { x ->
        field.indices.map { y ->
            field[field.lastIndex - y][x]
        }
    }

fun part2(input: List<String>, iterations: Int = 1000000000): Int {
    val startField = input.map { it.toList() }
    val visited = mutableMapOf<Field, Int>()
    var loopSize = 0
    val fields = generateSequence(startField) { field ->
        generateSequence(field) { rotatedCW(slideUp(it)) }.drop(4).first()
    }
        .withIndex()
        .takeWhile { (i, field) ->
            loopSize = i - visited.getOrPut(field) { i }
            loopSize == 0
        }
        .map { it.value }
        .toList()
    val loopStart = fields.size - loopSize

    return getFieldScore(
        fields[loopStart + (iterations - loopStart) % loopSize]
    )
}

fun main() {
    val testInput = readInput("Day14_test")

    expect(part1(testInput), 136)
    expect(part2(testInput), 64)

    val input = readInput("Day14")

    println(part1(input))
    println(part2(input))
}
