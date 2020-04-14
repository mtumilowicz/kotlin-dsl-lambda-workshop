package dsl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FsmTest {

    @Test
    fun a() {
//        when:
        val fsm = Fsm.create(initialState = "locked") {
            add { on("coin").from("locked").into("unlocked") }
            add { on("pass").from("unlocked").into("locked") }
        }

//        then:
        Assertions.assertEquals(State("locked"), fsm.initial)
        Assertions.assertEquals(2, fsm.transitions.size)
    }
}