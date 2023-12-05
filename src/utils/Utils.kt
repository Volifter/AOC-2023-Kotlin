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
fun getGCD(vararg values: Int): Int =
    values.toSet().reduce { left, right ->
        var a = left
        var b = right

        while (a != b) {
            when {
                (a == 0) -> return@reduce b
                (b == 0) -> return@reduce a
                (a > b) -> a -= b * (a / b)
                else -> b -= a * (b / a)
            }
        }

        return@reduce a
    }

/**
 * Computes the LCM of its arguments
 */
fun getLCM(vararg values: Int): Int = values.toSet().reduce { a, b ->
    maxOf(a, b) / getGCD(a, b) * minOf(a, b)
}
