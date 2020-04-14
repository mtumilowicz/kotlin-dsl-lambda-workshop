//package dsl
//
//import io.kotest.core.spec.style.BehaviorSpec
//import io.kotest.matchers.shouldBe
//
//class FsmTests : BehaviorSpec({
//    Given("An EntityManager 1") {
//        val fsm = Fsm.create(initialState = "locked") {
//            add {
//                on("coin")
//                from("locked")
//                into("unlocked")
//            }
//            add {
//                on("pass")
//                from("unlocked")
//                into("locked")
//            }
//        }
//        When("creating a new entity") {
//            Then("each entity is given a unique id") {
//                fsm.initial shouldBe State("locked")
//                fsm.transitions.size shouldBe 2
//            }
//        }
//    }
//})