package dsl

class FsmSpec(
    val transitions: MutableList<TransitionSpec.() -> TransitionSpec> = mutableListOf(),
    val initialState: String
) {

    fun add(transitionRecipe: TransitionSpec.() -> TransitionSpec): FsmSpec {
        transitions.add(transitionRecipe)
        return this
    }

    fun build(): Fsm {
        val map = transitions.map { func -> TransitionSpec().func().build() }
            .associateBy({ it.event }, { it.stateFlow })
        return Fsm(
            transitions = map,
            initial = State(initialState),
            state = State(initialState)
        )
    }
}

fun main() {
    val x = FsmSpec(initialState = "locked").apply {
        add { on("coin").from("locked").into("unlocked") }
        add { on("pass").from("unlocked").into("locked") }
    }

    println(x.build())
}