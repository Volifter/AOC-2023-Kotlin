package day19

import utils.*

typealias Part = Map<Char, Int>

typealias PartRange = Map<Char, IntRange>

data class Condition(val label: Char, val isLess: Boolean, val value: Int) {
    fun test(part: Part): Boolean =
        (if (isLess) part[label]!! < value else part[label]!! > value)

    fun splitPartRange(
        partRange: PartRange,
        isReversed: Boolean = false
    ): PartRange? {
        val range = partRange[label]!!
        val split = if (isReversed) {
            if (isLess)
                value..range.last
            else
                range.first..value
        } else {
            if (isLess)
                range.first..<value
            else
                value + 1..range.last
        }

        return (partRange + mapOf(label to split))
            .takeIf { split.last >= split.first }
    }
}

data class Rule(val condition: Condition?, val label: String) {
    fun test(part: Part) =
        condition?.test(part) ?: true
}

fun parseRules(lines: List<String>): Map<String, List<Rule>> =
    lines.associate { line ->
        val parts = line.split("{")
        val rules = parts[1].dropLast(1).split(",").map { rule ->
            val (key, value) = rule.split(":") + listOf(null)

            value
                ?.let {
                    val (label, amount) = key!!.split("<", ">")

                    Rule(
                        Condition(
                            label.single(),
                            key.contains('<'),
                            amount.toInt()
                        ),
                        it
                    )
                }
                ?: Rule(null, key!!)
        }

        parts[0] to rules
    }

fun parseParts(lines: List<String>): List<Part> = lines.map { line ->
    line
        .substring(1..<line.lastIndex)
        .split(",")
        .map { it.split("=") }
        .associate { (k, v) -> k.single() to v.toInt() }
}

fun parseInput(input: List<String>): Pair<Map<String, List<Rule>>, List<Part>> =
    groupLines(input).let { (rules, parts) ->
        Pair(
            parseRules(rules),
            parseParts(parts)
        )
    }

fun shouldAcceptPart(part: Part, rulesMap: Map<String, List<Rule>>): Boolean =
    generateSequence("in") { label ->
        rulesMap[label]
            ?.find { it.test(part) }
            ?.label
    }.last() == "A"

fun part1(input: List<String>): Int {
    val (rules, parts) = parseInput(input)

    return parts
        .filter { shouldAcceptPart(it, rules) }
        .sumOf { it.values.sum() }
}

fun processPartRange(
    initialPartRange: PartRange,
    rulesMap: Map<String, List<Rule>>,
    label: String
): Sequence<PartRange> = sequence {
    if (label == "A")
        return@sequence yield(initialPartRange)

    if (label == "R")
        return@sequence

    var partRange = initialPartRange

    rulesMap[label]!!.forEach { rule ->
        if (rule.condition == null)
            return@sequence yieldAll(
                processPartRange(partRange, rulesMap, rule.label)
            )

        rule.condition.splitPartRange(partRange)?.let {
            yieldAll(processPartRange(it, rulesMap, rule.label))
        }

        partRange = rule.condition.splitPartRange(partRange, true)
            ?: return@sequence
    }
}

fun part2(input: List<String>): Long {
    val rules = parseRules(input.takeWhile { it.isNotEmpty() })
    val partRange = "xmas".associateWith { 1..4000 }

    return processPartRange(partRange, rules, "in")
        .sumOf { ranges ->
            ranges.values.map { it.last - it.first + 1 }.fold(1L, Long::times)
        }
}

fun main() {
    val testInput = readInput("Day19_test")

    expect(part1(testInput), 19114)
    expect(part2(testInput), 167409079868000)

    val input = readInput("Day19")

    println(part1(input))
    println(part2(input))
}
