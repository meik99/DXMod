package com.rynkix.dxmod.roller

import androidx.annotation.VisibleForTesting
import java.util.logging.Logger
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**

/*
    E = T EX
    EX = + T EX
        | - T EX
        | EOF
    T = F TX
    TX = * F TX
        | / F TX
        | EOF
    F = a G d G
        | d G d G
        | G FX
    FX = d G
        | EOF
    G = ( E )
        | id
    A = a
    D = d
*/
 * */

class DiceRoller(
    val random: Random = Random.Default,
) {
    private val tag = DiceRoller::class.java.simpleName

    private var result: Int = 0
    private var textResult: String = ""
    private var roll: String = ""
    private var currentCharacter: Char = '-'
    private var currentIndex: Int = 0
    private val responses = mutableListOf<String>()

    fun ExecuteRollForResponse(input: String): List<String> {
        ExecuteRoll(input)
        return responses
    }

    fun ExecuteRoll(
        input: String
    ): Int {
        try {
            roll = input.replace(" ", "")
            currentIndex = 0
            currentCharacter = input[currentIndex]
            textResult = ""
            result = 0
            responses.clear()

            result = E()

            responses.add(
                "$input ="
            )
            responses.add("\t$result")
            return result
        } catch (e: Exception) {
            responses.add("invalid input: $input")
            if (e.message != null) responses.add(e.message!!)
            return -1
        }
    }

    private fun E(): Int {
        return EX(T())
    }

    private fun EX(operand1: Int): Int {
        if (eof()) {
            return operand1
        }

        val exResult = when(val operator = currentCharacter) {
            '+' -> {
                inc()
                val operand2 = T()
                val message = "$operand1 $operator $operand2 = ${operand1 + operand2}"
                responses.add(message)

                EX(operand1 + operand2)
            }
            '-' -> {
                inc()
                val operand2 = T()
                val message = "$operand1 $operator $operand2 = ${operand1 - operand2}"
                responses.add(message)

                EX(operand1 - operand2)
            }
            else -> {
                operand1
            }
        }

        return exResult
    }

    private fun T(): Int {
        return TX(F())
    }

    private fun TX(operand1: Int): Int {
        if (eof()) {
            return operand1
        }

        val txResult = when(val operator = currentCharacter) {
            '*' -> {
                inc()

                val operand2 = F()
                val message = "$operand1 $operator $operand2 = ${operand1 * operand2}"
                responses.add(message)

                TX(operand1 * operand2)
            }
            '×' -> {
                inc()
                val operand2 = F()
                val message = "$operand1 $operator $operand2 = ${operand1 * operand2}"
                responses.add(message)

                TX(operand1 * operand2)
            }
            '/' -> {
                inc()
                val operand2 = F()
                val message = "$operand1 $operator $operand2 = ${operand1 / operand2}"
                responses.add(message)

                TX(operand1 / operand2)
            }
            else -> {
                operand1
            }
        }

        return txResult
    }

    /*
        F = a F
            | d F
            | G FX
        FX = d G
            | EOF
        G = ( E )
            | id
    * */
    private fun F(): Int {
        val advantage = currentCharacter == 'a'
        val disadvantage = currentCharacter == 'd'

        if (advantage || disadvantage) {
            inc()
        }

        return FX(G(), advantage, disadvantage)
    }

    private fun FX(operand1: Int, advantage: Boolean = false, disadvantage: Boolean = false): Int {
        if (eof()) {
            return operand1
        }

        if (currentCharacter == 'd') {
            inc()
            val operand2 = G()
            var result1 = 0
            var result2 = 0

            (1..operand1).forEach { _ ->
                val dieCast = random.nextInt(1, operand2 + 1)
                result1 += dieCast
            }

            if (advantage || disadvantage) {
                (1..operand1).forEach { _ ->
                    val dieCast = random.nextInt(1, operand2 + 1)
                    result2 += dieCast
                }

            } else {
                val message = "rolling ${operand1}d$operand2 for $result1"
                Logger.getLogger(tag).info(
                    message
                )
                responses.add(message)

                return result1
            }

            val finalRoll = if (advantage) max(result1, result2)
                else min(result1, result2)

            val message = "rolling ${operand1}d$operand2 with ${
                if (advantage) "advantage"
                else "disadvantage"
                } for $finalRoll out of [${result1}, ${result2}]"

            Logger.getLogger(tag).info(
                message
            )
            responses.add(message)

            return finalRoll
        }

        return operand1
    }

    private fun G(): Int {
        return when(currentCharacter) {
            '(' -> {
                inc()
                val result = E()
                // Terminating ')'

                if (!eof()) {
                    inc()
                }

                result
            }
            else -> num()
        }
    }

    private fun num(): Int {
        var num = ""

        while (currentCharacter.isDigit()) {
            num += currentCharacter

            if (eof()) {
                break
            }

            inc()
        }

        return num.toInt()
    }

    fun inc() {
        currentIndex++
        currentCharacter = roll[currentIndex]
    }

    fun eof(): Boolean {
        return currentIndex >= roll.length - 1
    }
}
