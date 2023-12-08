package day08

import utils.*

data class Node(val name: String) {
    lateinit var left: Node
    lateinit var right: Node

    fun move(dir: Char) = when {
        dir == 'L' -> left
        else -> right
    }

    override fun toString(): String = "Node($name, ${left.name}, ${right.name})"
}

fun parseInput(input: List<String>): Pair<String, Map<String, Node>> =
    Pair(
        input.first(),
        input
            .drop(2)
            .fold(mutableMapOf()) { dict, line ->
                val (name, leftName, rightName) = line.split(" = (", ", ", ")")
                val (node, left, right) = listOf(name, leftName, rightName)
                    .map { dict.getOrPut(it) { Node(it) } }

                node.left = left
                node.right = right

                dict
            }
    )

fun getPathIterator(path: String): Iterator<Char> =
    sequence { while (true) yieldAll(path.asSequence()) }.iterator()

fun part1(input: List<String>): Int {
    val (instructions, nodesMap) = parseInput(input)
    val path = getPathIterator(instructions)

    return generateSequence(nodesMap["AAA"]!!) { node ->
        node.takeUnless { it.name == "ZZZ" }?.move(path.next())
    }.count() - 1
}

fun findLoops(
    start: Node,
    instructions: String
): Pair<Pair<Int, Int>, List<Int>> {
    val passed = mutableMapOf<Pair<Node, Int>, Int>()
    val ends = mutableListOf<Int>()
    val (node, length) = generateSequence(Pair(start, 0)) { (node, i) ->
        val j = i % instructions.length
        val key = Pair(node, j)

        passed[key]?.let { return@generateSequence null }

        passed[key] = i

        if (node.name.endsWith('Z'))
            ends.add(i)

        Pair(node.move(instructions[j]), i + 1)
    }.last()

    val loopStart = passed[Pair(node, length % instructions.length)]!!
    val loopSize = length - loopStart

    return Pair(Pair(loopStart, loopSize), ends.sorted())
}

fun part2(input: List<String>): Long {
    val (instructions, nodesMap) = parseInput(input)
    val starting = nodesMap.values.filter { it.name.endsWith('A') }
    val loops = starting.map { node -> findLoops(node, instructions) }

    check(loops.all { it.first.second == it.second.last() })

    return getLCM(*loops.map { it.first.second.toLong() }.toLongArray())
}

fun main() {
    expect(part1(readInput("Day08_test_a")), 2)
    expect(part1(readInput("Day08_test_b")), 6)
    expect(part2(readInput("Day08_test_c")), 6L)

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}
