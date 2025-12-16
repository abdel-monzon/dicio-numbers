package org.dicio.numbers.lang.es

import org.dicio.numbers.test.EntityTest
import org.dicio.numbers.test.assertEquals
import org.dicio.numbers.unit.Number
import org.junit.Test

class ExtractNumbersTest : EntityTest() {
    
    @Test
    fun testNumberSimple() {
        // Números simples
        assertEquals(Number(0), "cero", p::extractNumber)
        assertEquals(Number(5), "cinco", p::extractNumber)
        assertEquals(Number(12), "doce", p::extractNumber)
        assertEquals(Number(20), "veinte", p::extractNumber)
        assertEquals(Number(100), "cien", p::extractNumber)
    }

    @Test
    fun testNumberComplex() {
        // Números complejos
        assertEquals(Number(1234), "mil doscientos treinta y cuatro", p::extractNumber)
        assertEquals(Number(1000000), "un millón", p::extractNumber)
        assertEquals(Number(2500000), "dos millones quinientos mil", p::extractNumber)
    }

    @Test
    fun testDecimal() {
        // Decimales
        assertEquals(Number(3.14), "tres coma catorce", p::extractNumber)
        assertEquals(Number(0.5), "cero coma cinco", p::extractNumber)
    }

    @Test
    fun testOrdinal() {
        // Ordinales
        assertEquals(Number(1, isOrdinal = true), "primero", p::extractNumber)
        assertEquals(Number(2, isOrdinal = true), "segundo", p::extractNumber)
        assertEquals(Number(10, isOrdinal = true), "décimo", p::extractNumber)
    }

    @Test
    fun testFraction() {
        // Fracciones
        assertEquals(Number(1, 2), "un medio", p::extractNumber)
        assertEquals(Number(3, 4), "tres cuartos", p::extractNumber)
    }

    @Test
    fun testNegative() {
        // Negativos
        assertEquals(Number(-5), "menos cinco", p::extractNumber)
        assertEquals(Number(-20), "menos veinte", p::extractNumber)
    }
}

