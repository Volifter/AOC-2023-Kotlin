package day03

import utils.*

typealias Coords = Coords2D

data class EnginePart(val symbol: Char, val numbers: List<Int>)

fun getSymbolNumbers(input: List<String>): Sequence<EnginePart> {
    val size = Coords(input.first().length, input.size)

    return Coords.iterateOnField(input).mapNotNull { coords ->
        val c = input[coords.y][coords.x]

        if (c.isDigit() || c == '.')
            return@mapNotNull null

        val visited = mutableSetOf<Coords>()
        val numbers = coords.neighbors
            .filter {
                it in size
                    && !visited.contains(it)
                    && input[it.y][it.x].isDigit()
            }
            .map { (newX, newY) ->
                val startX = generateSequence(newX) { v ->
                    (v - 1).takeIf {
                        it >= 0 && input[newY][it].isDigit()
                    }
                }.last()

                generateSequence(Coords(startX, newY)) { prev ->
                    (prev + Coords(1, 0)).takeIf {
                        it in size && input[it.y][it.x].isDigit()
                    }
                }
                    .onEach { visited.add(it) }
                    .map { input[it.y][it.x].digitToInt() }
                    .reduce { acc, digit -> acc * 10 + digit }
            }
            .toList()

        EnginePart(c, numbers)
    }
}

fun part1(input: List<String>): Int = getSymbolNumbers(input)
    .sumOf { it.numbers.sum() }

fun part2(input: List<String>): Int = getSymbolNumbers(input)
    .filter { it.symbol == '*' && it.numbers.size == 2 }
    .sumOf { it.numbers.reduce(Int::times) }

fun main() {
    val testInput = readInput("Day03_test")

    expect(part1(testInput), 4361)
    expect(part2(testInput), 467835)

    val input = readInput("Day03")

    println(part1(input))
    println(part2(input))
}
