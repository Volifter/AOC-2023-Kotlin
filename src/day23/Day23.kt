package day23

import utils.*

typealias Coords = Coords2D

class Node(val position: Coords) {
    val neighbors = mutableMapOf<Node, Int>()
}

fun getOffsets(c: Char): Set<Coords> =
    when (c) {
        '.' -> setOf(
            Coords(0, -1),
            Coords(1, 0),
            Coords(0, 1),
            Coords(-1, 0)
        )
        '^' -> setOf(Coords(0, -1))
        '>' -> setOf(Coords(1, 0))
        'v' -> setOf(Coords(0, 1))
        '<' -> setOf(Coords(-1, 0))
        else -> throw IllegalStateException("invalid character $c")
    }

fun parseInput(
    input: List<String>,
    useSlopes: Boolean = true
): Map<Coords, Node> {
    val nodes = buildMap {
        Coords.iterateOnField(input).forEach { position ->
            if (input[position.y][position.x] != '#')
                set(position, Node(position))
        }
    }
    fun getChar(position: Coords) =
        input[position.y][position.x].takeIf { useSlopes } ?: '.'

    nodes.values.forEach { node ->
        getOffsets(getChar(node.position))
            .filter {
                node.position + it in nodes
                    && it in getOffsets(getChar(node.position + it))
            }
            .forEach {
                nodes[node.position + it]!!.let { other ->
                    node.neighbors[other] = 1
                }
            }
    }

    return nodes
}

fun simplifyNodes(nodes: Map<Coords, Node>): Map<Coords, Node> {
    nodes.values
        .asSequence()
        .filter { it.neighbors.size == 2 }
        .mapNotNull { node ->
            val (neighborA, neighborB) = node.neighbors.keys.toList()

            listOf(neighborA, node, neighborB).takeIf {
                node in neighborA.neighbors && node in neighborB.neighbors
            }
        }
        .forEach { (neighborA, node, neighborB) ->
            val size = (
                neighborA.neighbors[node]!!
                    + neighborB.neighbors[node]!!
            )

            neighborA.neighbors[neighborB] = size
            neighborA.neighbors.remove(node)

            neighborB.neighbors[neighborA] = size
            neighborB.neighbors.remove(node)
        }

    return nodes
}

fun solve(
    current: Node,
    target: Node,
    visited: Set<Node> = setOf(),
    distance: Int = 0
): Int? {
    if (current == target)
        return distance

    return current.neighbors
        .filter { it.key !in visited }
        .mapNotNull { (neighbor, neighborDistance) ->
            solve(
                neighbor,
                target,
                visited + neighbor,
                distance + neighborDistance
            )
        }
        .maxOrNull()
}

fun part1(input: List<String>): Int {
    val nodes = simplifyNodes(parseInput(input))

    return solve(nodes.values.first(), nodes.values.last())!!
}

fun part2(input: List<String>): Int {
    val nodes = simplifyNodes(parseInput(input, false))

    return solve(nodes.values.first(), nodes.values.last())!!
}

fun main() {
    val testInput = readInput("Day23_test")

    expect(part1(testInput), 94)
    expect(part2(testInput), 154)

    val input = readInput("Day23")

    println(part1(input))
    println(part2(input))
}
