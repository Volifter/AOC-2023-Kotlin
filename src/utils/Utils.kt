package utils

import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> =
    File("inputs", "$name.txt").readLines()

/**
 * Groups lines by a separator line
 */
fun groupLines(lines: List<String>): List<List<String>> =
    lines.fold(listOf(listOf())) { acc, line ->
        if (line.isEmpty())
            acc + listOf(listOf())
        else
            acc.dropLast(1) + listOf(acc.last() + line)
    }

/**
 * A verbose encapsulation of check()
 */
fun <T> expect(got: T, expected: T) {
    try {
        check(got == expected)
    } catch (exception: IllegalStateException) {
        System.err.println("Assertion failed: expected $expected, got $got")

        throw exception
    }

    println("Assertion passed: $got == $got")
}

/**
 * Computes the GCD of its arguments
 */
fun getGCD(vararg values: Long): Long =
    values.toSet().reduce { left, right ->
        var a = left
        var b = right

        while (a != b) {
            when {
                (a == 0L) -> return@reduce b
                (b == 0L) -> return@reduce a
                (a > b) -> a -= b * (a / b)
                else -> b -= a * (b / a)
            }
        }

        return@reduce a
    }

/**
 * Computes the LCM of its arguments
 */
fun getLCM(vararg values: Long): Long = values.toSet().reduce { a, b ->
    maxOf(a, b) / getGCD(a, b) * minOf(a, b)
}

/**
 * Computes the modular inverse of a value by modulus mod
 */
fun getModularInverse(value: Long, mod: Long): Long {
    if (mod == 1L)
        return 0

    var (n, m, x, y) = listOf(value, mod, 1, 0)

    while (n > 1) {
        if (m == 0L)
            throw Error("not inversible")

        val (nextMod, nextN, nextX, nextY) = listOf(n % m, m, y, x - n / m * y)

        m = nextMod
        n = nextN
        x = nextX
        y = nextY
    }

    return (x + mod) % mod
}

/*
 * Chinese Remainder Theorem implementation. Takes a list of tuples (rem, mod)
 * where each rem is a remainder and each mod is a modulus and returns an
 * integer `n` for which each `n % mod = rem`.
 *
 * Assumes all `mod`s are coprime.
 */
fun getCRT(values: List<Pair<Int, Int>>): Long {
    val prod = values.fold(1L) { acc, (_, mod) -> acc * mod }

    return values.sumOf { (rem, mod) ->
        val p = prod / mod

        rem * getModularInverse(p, mod.toLong()) * p
    } % prod
}
