package day02

import utils.*

data class GameCubes(val red: Int, val green: Int, val blue: Int) {
    val power get() = red * green * blue

    fun fitsGame(other: GameCubes): Boolean =
        other.red <= red
            && other.green <= green
            && other.blue <= blue
}

fun parseInput(input: List<String>): List<Pair<Int, List<GameCubes>>> =
    input.map { line ->
        val parts = line.split(": ")
        val id = parts[0].substring(5).toInt()
        val games = parts[1].split("; ").map { entries ->
            val counts = entries
                .split(", ")
                .associate { entry ->
                    entry.split(" ").let { (count, color) ->
                        color to count.toInt()
                    }
                }

            GameCubes(
                counts["red"] ?: 0,
                counts["green"] ?: 0,
                counts["blue"] ?: 0
            )
        }

        Pair(id, games)
    }

fun getMinPossibleCubes(games: List<GameCubes>): GameCubes =
    GameCubes(
        games.maxOf { it.red },
        games.maxOf { it.green },
        games.maxOf { it.blue }
    )

fun part1(input: List<String>, target: GameCubes = GameCubes(12, 13, 14)): Int =
    parseInput(input)
        .filter { target.fitsGame(getMinPossibleCubes(it.second)) }
        .sumOf { it.first }

fun part2(input: List<String>): Int =
    parseInput(input).sumOf { getMinPossibleCubes(it.second).power }

fun main() {
    val testInput = readInput("Day02_test")

    expect(part1(testInput), 8)
    expect(part2(testInput), 2286)

    val input = readInput("Day02")

    println(part1(input))
    println(part2(input))
}
