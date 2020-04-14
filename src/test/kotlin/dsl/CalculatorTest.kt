package dsl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CalculatorTest {
    private val calculator = Calculator()
 
    @Test
    fun whenAdding1and3_thenAnswerIs4() {
        Assertions.assertEquals(4, calculator.add(1, 3))
    }
}