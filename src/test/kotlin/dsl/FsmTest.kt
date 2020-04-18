package dsl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.maps.haveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave

class FsmTest : BehaviorSpec({
    given("a broomstick") {
        `when`("I sit on it") {
            val fsm = Fsm.create(initialState = "locked") {
                add { this on "coin" from "locked" into "unlocked" }
                add { this on "pass" from "unlocked" into "locked" }
            }
            then("I should be able to fly") {
                fsm.initial shouldBe State("locked")
                fsm.transitions shouldHave haveSize(2)
            }
        }
    }

    given("2222") {
        `when`("I sit on it") {
            val fsmSpec = FsmSpec(initialState = "locked")
            val fsm = fsmSpec {
                add { this on "coin" from "locked" into "unlocked" }
                add { this on "pass" from "unlocked" into "locked" }
            }

            then("I should be able to fly") {
                fsm.initial shouldBe State("locked")
                fsm.transitions shouldHave haveSize(2)
            }
        }
    }
})