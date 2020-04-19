package dsl.answers

class FsmSpec(
    private val transitions: MutableList<Transition> = mutableListOf(),
    val initialState: String
) {

    fun add(transitionRecipe: TransitionSpec.() -> Unit) {
        transitions.add(Transition.create(transitionRecipe))
    }

    fun build(): Fsm {
        val map = transitions.associateBy({ it.event }, { it.stateFlow })
        return Fsm(
            transitions = map,
            initial = State(initialState),
            state = State(initialState)
        )
    }

    operator fun invoke(block: FsmSpec.() -> Unit): Fsm {
        block()
        return build()
    }
}