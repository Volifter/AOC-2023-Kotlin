package day24

import kotlin.math.roundToLong
import kotlin.math.sign

import utils.*

data class Coords(val x: Double, val y: Double, val z: Double) {
    operator fun plus(other: Coords) =
        Coords(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Coords) =
        Coords(x - other.x, y - other.y, z - other.z)

    operator fun times(factor: Double) =
        Coords(x * factor, y * factor, z * factor)

    operator fun times(other: Coords) =
        Coords(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )

    operator fun div(factor: Double) =
        Coords(x / factor, y / factor, z / factor)

    infix fun dot(other: Coords) =
        x * other.x + y * other.y + z * other.z
}

data class Hail(val position: Coords, val velocity: Coords)

fun parseInput(input: List<String>): List<Hail> = input.map { line ->
    val parts = line
        .split(",", " ", "@")
        .filter(String::isNotEmpty)
        .map(String::toDouble)
    val position = Coords(parts[0], parts[1], parts[2])
    val velocity = Coords(parts[3], parts[4], parts[5])

    Hail(position, velocity)
}

fun part1(input: List<String>, range: LongRange = 7L..27L): Int {
    val hails = parseInput(input)

    return hails.withIndex().sumOf { (i, hailA) ->
        hails.drop(i + 1).count { hailB ->
            val ak = 1.0 * hailA.velocity.y / hailA.velocity.x
            val ab = hailA.position.y - ak * hailA.position.x
            val bk = 1.0 * hailB.velocity.y / hailB.velocity.x
            val bb = hailB.position.y - bk * hailB.position.x
            val x = (bb - ab) / (ak - bk)
            val y = ak * x + ab

            (
                !x.isInfinite()
                && (x - hailA.position.x).sign == hailA.velocity.x.sign
                && (y - hailA.position.y).sign == hailA.velocity.y.sign
                && (x - hailB.position.x).sign == hailB.velocity.x.sign
                && (y - hailB.position.y).sign == hailB.velocity.y.sign
                && x >= range.first && x <= range.last
                && y >= range.first && y <= range.last
            )
        }
    }
}

fun getSlope(hailA: Hail, hailB: Hail): Pair<Coords, Double> {
    val deltaPos = hailA.position - hailB.position
    val deltaVel = hailA.velocity - hailB.velocity

    return Pair(
        deltaPos * deltaVel,
        deltaPos dot (hailA.velocity * hailB.velocity)
    )
}

fun getRockPosition(hailA: Hail, hailB: Hail, hailC: Hail): Coords {
    val (a, aRate) = getSlope(hailA, hailB)
    val (b, bRate) = getSlope(hailA, hailC)
    val (c, cRate) = getSlope(hailB, hailC)

    val delta = (b * c * aRate + c * a * bRate + a * b * cRate) / (a dot b * c)
    val deltaA = hailA.velocity - delta
    val deltaB = hailB.velocity - delta
    val deltaProd = deltaA * deltaB

    val coords = (
        deltaProd * (hailA.position dot deltaProd)
        + deltaA * (hailB.position * deltaB dot deltaProd)
        - deltaB * (hailA.position * deltaA dot deltaProd)
    )

    return coords / (deltaProd dot deltaProd)
}

fun part2(input: List<String>): Long {
    val hails = parseInput(input).take(3)
    val position = getRockPosition(hails[0], hails[1], hails[2])

    return (position.x + position.y + position.z).roundToLong()
}

fun main() {
    val testInput = readInput("Day24_test")

    expect(part1(testInput, 7L..27L), 2)
    expect(part2(testInput), 47)

    val input = readInput("Day24")

    println(part1(input, 200000000000000..400000000000000))
    println(part2(input))
}
