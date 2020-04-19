package answers

import dsl.Fsm
import dsl.FsmSpec
import dsl.State
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.maps.haveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave

class FsmTestExample : BehaviorSpec({

    given("fsm") {
        val fsm = Fsm.create(initialState = "locked") {
            add { this on "coin" from "locked" into "unlocked" }
            add { this on "pass" from "unlocked" into "locked" }
        }
        then("I could move from state to state") {
            fsm.initial shouldBe State("locked")
            fsm.transitions shouldHave haveSize(2)
            fsm["coin"] shouldBe State("unlocked")
            fsm["pass"] shouldBe State("locked")
        }
    }

    given("fsm from fsm spec") {
        val fsmSpec = FsmSpec(initialState = "locked")
        val fsm = fsmSpec {
            add { this on "coin" from "locked" into "unlocked" }
            add { this on "pass" from "unlocked" into "locked" }
        }

        then("I could move from state to state") {
            fsm.initial shouldBe State("locked")
            fsm.transitions shouldHave haveSize(2)
            fsm["coin"] shouldBe State("unlocked")
            fsm["pass"] shouldBe State("locked")
        }
    }
})