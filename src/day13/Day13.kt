package day13

import utils.*

fun parseInput(input: List<String>): List<Int> = input.map { 1 }

fun rotatedCCW(field: List<String>): List<String> =
    field.first().indices.map { x ->
        field.indices.joinToString("") { y ->
            "" + field[y][field.first().lastIndex - x]
        }
    }

fun getReflections(line: String, maxErrors: Int = 0): Map<Int, Int> =
    (1..line.lastIndex).mapNotNull { i ->
        Pair(
            i,
            generateSequence(Pair(i - 1, i)) { (l, r) ->
                Pair(l - 1, r + 1).takeIf { l > 0 && r < line.lastIndex }
            }.count { (l, r) -> line[l] != line[r] }
        ).takeIf { it.second <= maxErrors }
    }.toMap()

fun solveForField(field: List<String>, errors: Int): Int? =
    field
        .map { line -> getReflections(line, errors) }
        .reduce { prev, next ->
            prev.keys
                .filter { it in next }
                .associateWith { prev[it]!! + next[it]!! }
        }
        .mapNotNull { (key, value) -> key.takeIf { value == errors } }
        .singleOrNull()

fun solve(input: List<String>, errors: Int = 0) =
    groupLines(input).sumOf { field ->
        solveForField(field, errors)
            ?: solveForField(rotatedCCW(field), errors)?.let { it * 100 }
            ?: throw Error("no valid reflections")
    }

fun main() {
    val testInput = readInput("Day13_test")

    expect(solve(testInput), 405)
    expect(solve(testInput, 1), 400)

    val input = readInput("Day13")

    println(solve(input))
    println(solve(input, 1))
}
