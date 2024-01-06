package day22

import utils.*

typealias Box = Box3D

typealias Coords = Coords3D

class Brick(var box: Box) {
    val below = mutableSetOf<Brick>()
    val above = mutableSetOf<Brick>()

    val canDisintegrate get() = above.all { it.below.size > 1 }

    fun getReactionSize(removed: MutableSet<Brick> = mutableSetOf(this)): Int =
        above.sumOf {
            if (it.below.all { brick -> brick in removed }) {
                removed += it
                1 + it.getReactionSize(removed)
            } else {
                0
            }
        }
}

fun parseInput(input: List<String>): List<Brick> = input
    .map { line ->
        val (partA, partB) = line.split("~")
        val (cornerA, cornerB) = listOf(partA, partB).map { part ->
            val (x, y, z) = part.split(",").map(String::toInt)

            Coords(x, y, z)
        }

        Brick(Box.fromCorners(cornerA, cornerB))
    }
    .sortedBy { it.box.start.z }

fun dropBricks(bricks: List<Brick>) {
    bricks.forEachIndexed { i, brick ->
        val fallArea = Box(
            brick.box.start.run { Coords(x, y, 1) },
            brick.box.end.run { Coords(x, y, z - 1) }
        )
        val intersections = bricks
            .asSequence()
            .take(i)
            .filter { it.box.intersect(fallArea).isNotEmpty }
            .sortedByDescending { it.box.end.z }
        val newZ = intersections.firstOrNull()?.run { box.end.z + 1 } ?: 1

        brick.box -= Coords(0, 0, brick.box.start.z - newZ)

        intersections
            .takeWhile { it.box.end.z + 1 == newZ }
            .forEach { other ->
                brick.below.add(other)
                other.above.add(brick)
            }
    }
}

fun part1(input: List<String>): Int =
    parseInput(input)
        .also { dropBricks(it) }
        .count { it.canDisintegrate }

fun part2(input: List<String>): Int =
    parseInput(input)
        .also { dropBricks(it) }
        .sumOf { brick -> brick.getReactionSize() }

fun main() {
    val testInput = readInput("Day22_test")

    expect(part1(testInput), 5)
    expect(part2(testInput), 7)

    val input = readInput("Day22")

    println(part1(input))
    println(part2(input))
}
