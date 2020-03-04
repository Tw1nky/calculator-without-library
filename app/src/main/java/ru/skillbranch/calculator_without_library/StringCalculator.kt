package ru.skillbranch.calculator_without_library

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("NAME_SHADOWING")
class StringCalculator {
    private var result: String? = null
    private var debugging = false

    private fun parseValues(input: String): ArrayList<String> {
        var numValue = ""
        val values = ArrayList<String>()
        for (i in input.indices) {
            when (val chr = input[i]) {
                '-', '+', '*', '/', '(', ')' -> if (i == 0 && chr == '-') {
                    numValue += chr
                } else {
                    var chrBefore = chr
                    if (i > 0) {
                        chrBefore = input[i - 1]
                    }
                    if (chr == '-' && (chrBefore == '*' || chrBefore == '/')) {
                        numValue += chr
                    } else {
                        if (numValue.isNotEmpty()) values.add(parseValue(numValue))
                        numValue = ""
                        values.add(chr.toString())
                    }
                }
                else ->
                    numValue += chr
            }
        }
        if (numValue != "") values.add(numValue)
        return values
    }

    private fun parseValue(value: String): String {
        var value = value
        var result: String?

        if (value.toLowerCase() == "pi" || value.toLowerCase() == "π") {
            result = Math.PI.toString()
        } else if (value.contains("0x")) {
            value = value.replace("0x", "")
            result = value.toLong(16).toString()
        } else if (value.contains("0b")) {
            value = value.replace("0b", "")
            result = value.toLong(2).toString()
        } else {
            result = value.toDouble().toString()
        }
        return result
    }

    @Throws(NumberFormatException::class)
    private fun calculateRunner(input: String): String {
        var input = input
        input = input.trim { it <= ' ' }.replace(" ", "")
        val values =
            parseValues(input)
        if (values.size == 1) return parseValue(values[0])
        if (debugging) println(input)
        var nextBracket = -1
        var closedBracket = 0
        var bracketLevel = 0
        for (i in values.indices) {
            when (values[i]) {
                "(" -> {
                    if (nextBracket == -1) {
                        nextBracket = i
                        closedBracket = -1
                    }
                    bracketLevel++
                }
                ")" -> {
                    bracketLevel--
                    if (bracketLevel == 0) closedBracket = i
                }
            }
            if (nextBracket > -1 && bracketLevel == 0) {
                break
            }
        }
        if (nextBracket >= 0 && closedBracket > 0) {
            var operation = ""
            for (i in nextBracket + 1 until closedBracket) {
                operation += values[i]
            }
            val result = calculateRunner(operation)
            for (i in nextBracket + 1 until closedBracket + 1) {
                values.removeAt(nextBracket + 1)
            }
            values[nextBracket] = result
            operation = ""
            for (`val` in values) {
                operation += `val`
            }
            return calculateRunner(operation)
        }
        var nextOperationPosition = -1

        if (nextOperationPosition < 0) {
            for (i in values.indices) {
                when (values[i]) {
                   "*", "/" -> nextOperationPosition = i
                }
                if (nextOperationPosition >= 0) break
            }
            if (nextOperationPosition < 0) nextOperationPosition = 0
        }
        var result = 0.0
        var index = -1
        var operation = ""
        for (i in nextOperationPosition until values.size) {
            when (values[i]) {
                "*" -> {
                    result =
                        parseValue(values[i - 1]).toDouble() * parseValue(values[i + 1]).toDouble()
                    index = i
                }
                "/" -> {
                    result =
                        parseValue(values[i - 1]).toDouble() / parseValue(values[i + 1]).toDouble()
                    index = i
                }
                "+" -> {
                    result =
                        parseValue(values[i - 1]).toDouble() + parseValue(values[i + 1]).toDouble()
                    index = i
                }
                "-" -> {
                    result =
                        parseValue(values[i - 1]).toDouble() - parseValue(values[i + 1]).toDouble()
                    index = i
                }

            }
            if (index >= 0) {
                when (values.removeAt(index)) {
                    "ss"-> values[index - 1] =
                        result.toString()
                    else -> {
                        values[index - 1] =
                            result.toString()
                        values.removeAt(index)
                    }
                }
                for (`val` in values) operation += `val`
                break
            }
        }
        return calculateRunner(operation)
    }

    fun calculate(operation: String): String? {
        var lessThan = operation.indexOf("<")
        var biggerThan = operation.indexOf(">")
        val equals = operation.indexOf("=")
        if (lessThan > -1 || biggerThan > -1 || equals > -1) {
            val operations =
                when {
                    equals > -1 -> operation.split("=").toTypedArray()
                    lessThan > -1 -> operation.split(
                        "<"
                    ).toTypedArray()
                    else -> operation.split(">").toTypedArray()
                }
            if (equals > -1) {
                lessThan = operations[0].indexOf("<")
                biggerThan = operations[0].indexOf(">")
                if (lessThan > -1 || biggerThan > -1) {
                    operations[0] = if (lessThan > -1) operations[0].substring(
                        0,
                        operations[0].indexOf("<")
                    ) else operations[0].substring(0, operations[0].indexOf(">"))
                }
            }
            if (operations.size == 1) {
                result = "false"
                return result
            }
            result = calculateRunner(operations[0])
            val resultLeftDouble = resultAsDouble
            result = calculateRunner(operations[1])
            val resultRightDouble = resultAsDouble
            if (equals > -1) {
                result = if (lessThan > -1) {
                    (resultLeftDouble <= resultRightDouble).toString()
                } else if (biggerThan > -1) {
                    (resultLeftDouble >= resultRightDouble).toString()
                } else {
                    (resultLeftDouble == resultRightDouble).toString()
                }
            } else {
                if (lessThan > -1) {
                    result = (resultLeftDouble < resultRightDouble).toString()
                } else if (biggerThan > -1) {
                    result = (resultLeftDouble > resultRightDouble).toString()
                }
            }
        } else {
            result = calculateRunner(operation)
        }
        return result
    }
    
    private val resultAsDouble: Double
        get() = result!!.toDouble()
}
