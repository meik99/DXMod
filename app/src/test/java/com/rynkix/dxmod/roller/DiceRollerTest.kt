package com.rynkix.dxmod.roller

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.random.Random


class DiceRollerTest {
    private val tag = DiceRollerTest::class.java.simpleName

    lateinit var diceRoller: DiceRoller

    @Before
    fun setup() {
        diceRoller = DiceRoller(
            random = Random(1)
        )
    }

    @Test
    fun simpleCalculations() {
        val calculations =
            mapOf(
                Pair("1 + 2", 3),
                Pair("2 * 3", 6),
                Pair("4 / 2", 2),
                Pair("4 - 1", 3),
                Pair("5 + 2 * 3", 11),
                Pair("2 + 2 * 3 - 6 / 3", 6)
            )

        for ((calculation, expected) in calculations) {
            Logger.getLogger(tag).info(calculation)
            val result = diceRoller.ExecuteRoll(calculation)
            assertEquals(expected, result)
        }
    }

    @Test
    fun complexCalculations() {
        val calculations =
            mapOf(
                Pair("(1 + 2) * 3", 9),
                Pair("2 * (3 + 1)", 8),
                Pair("((4 - 2) / 2)", 1)
            )

        for ((calculation, expected) in calculations) {
            Logger.getLogger(tag).info(calculation)
            val result = diceRoller.ExecuteRoll(calculation)
            assertEquals(expected, result)
        }
    }


    @Test
    fun complexCalculationsWithDice() {
        val calculations =
            mapOf(
                Pair("(1 + 2d6) * 3", 18),
                Pair("2 * (3d10 + 1)", 46),
                Pair("((4 - 4d20) / 2)", -12),
                Pair("(((1 * 3) - 4d(20 / 2)) / 2)", -6)
            )

        for ((calculation, expected) in calculations) {
            Logger.getLogger(tag).info(calculation)
            val result = diceRoller.ExecuteRoll(calculation)
            assertEquals(expected, result)
        }
    }

    @Test
    fun complexCalculationsWithDiceAndAdvantage() {
        val calculations =
            mapOf(
                Pair("(1 + a 2d6) * 3", 21),
                Pair("2 * (d 3d10 + 1)", 24),
                Pair("((4 - d 4d20) / 2)", -10),
                Pair("(((1 * 3) - a 4d(20 / 2)) / 2)", -12),
                Pair("(((2d4)d6)d10)d20", 1253)
            )

        for ((calculation, expected) in calculations) {
            Logger.getLogger(tag).info(calculation)
            val result = diceRoller.ExecuteRoll(calculation)
            assertEquals(expected, result)
        }
    }
}