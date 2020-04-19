package dsl.workshop

import dsl.answers.State

class FsmSpecWorkshop(
    private val transitions: MutableList<TransitionWorkshop> = mutableListOf(),
    val initialState: String
) {

    fun add(transitionRecipe: TransitionSpecWorkshop.() -> Unit) {
        transitions.add(TransitionWorkshop.create(transitionRecipe))
    }

    fun build(): FsmWorkshop {
        val map = transitions.associateBy({ it.event }, { it.stateFlow })
        return FsmWorkshop(
            transitions = map,
            initial = State(initialState),
            state = State(initialState)
        )
    }

    operator fun invoke(block: FsmSpecWorkshop.() -> Unit): FsmWorkshop {
        block()
        return build()
    }
}