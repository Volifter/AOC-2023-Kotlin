package day20

import utils.*

open class Module(private val name: String) {
    val origins = mutableListOf<Module>()
    val destinations = mutableListOf<Module>()

    open fun sendPulse(pulse: Boolean, origin: Module): Boolean? = pulse

    fun connectTo(other: Module) {
        destinations.add(other)
        other.origins.add(this)
    }

    override fun toString(): String =
        "${javaClass.simpleName}($name -> ${destinations.map { it.name }})"
}

data class FlipFlopModule(val name: String): Module(name) {
    private var value = false

    override fun sendPulse(pulse: Boolean, origin: Module): Boolean? =
        if (pulse)
            null
        else
            (!value).also { value = it }

    override fun toString(): String = super.toString()
}

data class ConjunctionModule(val name: String): Module(name) {
    var sentLow = false

    private val activeOrigins = mutableSetOf<Module>()

    override fun sendPulse(pulse: Boolean, origin: Module): Boolean {
        if (pulse)
            activeOrigins.add(origin)
        else
            activeOrigins.remove(origin)

        return (activeOrigins.size != origins.size).also {
            sentLow = sentLow || it
        }
    }

    override fun toString(): String = super.toString()
}

fun parseInput(input: List<String>): Map<String, Module> {
    val modules = mutableMapOf("button" to Module("button"))

    input
        .map { line ->
            val (from, to) = line.split(" -> ")
            val name = from.dropWhile { it == '%' || it == '&' }
            val origin = when (from.first()) {
                '%' -> FlipFlopModule(name)
                '&' -> ConjunctionModule(name)
                else -> Module(name)
            }

            modules[name] = origin

            Pair(origin, to.split(", "))
        }
        .forEach { (origin, destinationNames) ->
            destinationNames.forEach { name ->
                origin.connectTo(modules[name]
                    ?: modules.getOrPut(name) { Module(name) }
                )
            }
        }

    modules["button"]!!.connectTo(modules["broadcaster"]!!)

    return modules
}

fun pushButton(root: Module): Pair<Int, Int> {
    var stack = listOf(Pair(root, false))
    var hiCount = 0
    var lowCount = 0

    while (stack.isNotEmpty()) {
        stack = buildList {
            stack.forEach { (module, pulse) ->
                module.destinations.forEach { childModule ->
                    if (pulse)
                        hiCount++
                    else
                        lowCount++

                    childModule.sendPulse(pulse, module)?.let {
                        add(Pair(childModule, it))
                    }
                }
            }
        }
    }

    return Pair(lowCount, hiCount)
}

fun part1(input: List<String>, count: Int = 1000): Long {
    val modules = parseInput(input)
    val button = modules["button"]!!
    val (low, hi) = generateSequence(Pair(0, 0)) { (lowCount, hiCount) ->
        pushButton(button).let { (newLow, newHi) ->
            Pair(lowCount + newLow, hiCount + newHi)
        }
    }.take(count + 1).last()

    return 1L * low * hi
}

fun part2(input: List<String>): Long {
    val modules = parseInput(input)
    val origins = modules["rx"]!!
        .origins.single()
        .origins.map { it as ConjunctionModule }
    val button = modules["button"]!!
    val cycles = mutableMapOf<Module, Int>()

    generateSequence(1) { i ->
        pushButton(button)

        origins.forEach { origin ->
            if (origin !in cycles && origin.sentLow)
                cycles[origin] = i
        }

        (i + 1).takeIf { cycles.size < origins.size }
    }.count()

    return getLCM(*cycles.values.map(Int::toLong).toLongArray())
}

fun main() {
    val testInputA = readInput("Day20_test_a")
    val testInputB = readInput("Day20_test_b")

    expect(part1(testInputA), 32000000)
    expect(part1(testInputB), 11687500)

    val input = readInput("Day20")

    println(part1(input))
    println(part2(input))
}
