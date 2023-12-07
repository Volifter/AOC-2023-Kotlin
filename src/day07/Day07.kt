package day07

import utils.*

val RANKS = listOf(
    listOf(1, 1, 1, 1, 1),
    listOf(1, 1, 1, 2),
    listOf(1, 2, 2),
    listOf(1, 1, 3),
    listOf(2, 3),
    listOf(1, 4),
    listOf(5)
)

interface IHand: Comparable<IHand> {
    val cards: List<Int>
    val value: Int
    val rank: Int
}

open class Hand(private val symbols: String, override val value: Int): IHand {
    override val cards = symbols.map { "23456789TJQKA".indexOf(it) }

    override val rank get() = RANKS.indexOf(
        cards
            .toSet()
            .map { cards.count { c -> c == it } }
            .sorted()
    )

    override fun compareTo(other: IHand): Int {
        rank.compareTo(other.rank).takeIf { it != 0 }?.let { return it }

        cards.zip(other.cards).map { (a, b) ->
            a.compareTo(b).takeIf { it != 0 }?.let { return it }
        }

        return 0
    }

    override fun toString(): String = "$symbols $value"
}

class JokerHand(symbols: String, value: Int) : Hand(symbols, value) {
    override val cards = symbols.map { "J23456789TQKA".indexOf(it) }

    override val rank get() = RANKS.indexOf(
        cards
            .toSet()
            .filter { it != 0 }
            .map { cards.count { c -> c == it } }
            .sorted()
            .let { frequencies ->
                if (frequencies.isEmpty())
                    listOf(5)
                else
                    (
                        frequencies.dropLast(1)
                        + listOf(cards.count { it == 0 } + frequencies.last())
                    )
            }
    )
}

fun parseInput(
    input: List<String>,
    constructor: (String, Int) -> IHand
): List<IHand> =
    input.map { line ->
        line.split(" ").let { (symbols, value) ->
            constructor(symbols, value.toInt())
        }
    }

fun solve(input: List<String>, constructor: (String, Int) -> IHand): Int =
    parseInput(input, constructor)
        .sorted()
        .withIndex()
        .sumOf { (i, hand) -> hand.value * (i + 1) }

fun part1(input: List<String>): Int = solve(input) { a, b -> Hand(a, b) }

fun part2(input: List<String>): Int = solve(input) { a, b -> JokerHand(a, b) }

fun main() {
    val testInput = readInput("Day07_test")

    expect(part1(testInput), 6440)
    expect(part2(testInput), 5905)

    val input = readInput("Day07")

    println(part1(input))
    println(part2(input))
}
