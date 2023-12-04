package day04

import utils.*

data class ScratchCard(
    val winningNumbers: Set<Int>,
    val foundNumbers: Set<Int>
) {
    val matches = foundNumbers.count { it in winningNumbers }
}

fun parseInput(input: List<String>): List<ScratchCard> =
    input.map { line ->
        line
            .split(": ")[1]
            .split(" | ")
            .map { part ->
                part
                    .split(" ")
                    .filterNot(String::isEmpty)
                    .map(String::toInt)
                    .toSet()
            }
            .let { (found, winning) -> ScratchCard(found, winning) }
    }

fun part1(input: List<String>): Int = parseInput(input)
    .sumOf {
        it.matches.let { count ->
            if (count > 0) 1 shl (count - 1) else 0
        }
    }

fun part2(input: List<String>): Int {
    val cards = parseInput(input)
    val counts = cards.map { 1 }.toMutableList()

    return cards.indices.sumOf { i ->
        val matches = cards[i].matches

        (i + 1..<minOf(i + 1 + matches, counts.size)).map { j ->
            counts[j] += counts[i]
        }

        counts[i]
    }
}

fun main() {
    val testInput = readInput("Day04_test")

    expect(part1(testInput), 13)
    expect(part2(testInput), 30)

    val input = readInput("Day04")

    println(part1(input))
    println(part2(input))
}
