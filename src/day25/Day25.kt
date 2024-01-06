package day25

import utils.*

class Node(val name: String) {
    val neighbors = mutableSetOf<Node>()
}

fun parseInput(input: List<String>): Map<String, Node> {
    val nodes = mutableMapOf<String, Node>()

    input.forEach { line ->
        val names = line.split(":", " ").filter(String::isNotEmpty)
        val nameA = names.first()

        names.drop(1).forEach { nameB ->
            val nodeA = nodes.getOrPut(nameA) { Node(nameA) }
            val nodeB = nodes.getOrPut(nameB) { Node(nameB) }

            nodeA.neighbors.add(nodeB)
            nodeB.neighbors.add(nodeA)
        }
    }

    return nodes
}

fun part1(input: List<String>, slices: Int = 3): Int {
    val nodes = parseInput(input)

    val visited = mutableSetOf(nodes.values.first())
    val leafNodes = visited.first().neighbors.toMutableSet()
    var slicesFound = false

    while (!slicesFound || leafNodes.size == slices) {
        val node = leafNodes.minBy { node ->
            node.neighbors.count { it !in visited }
        }

        visited.add(node)
        leafNodes.remove(node)
        leafNodes.addAll(node.neighbors.filter { it !in visited })

        slicesFound = slicesFound || leafNodes.size == slices
    }

    val size = visited.size - 1

    return size * (nodes.size - size)
}

fun main() {
    val testInput = readInput("Day25_test")

    expect(part1(testInput), 54)

    val input = readInput("Day25")

    println(part1(input))
}
