package dsl

class FsmSpec(
    private val transitions: MutableList<Transition> = mutableListOf(),
    val initialState: String
) {

    fun add(transitionRecipe: TransitionSpec.() -> TransitionSpec): FsmSpec {
        transitions.add(Transition.create(transitionRecipe))
        return this
    }

    fun build(): Fsm {
        val map = transitions.associateBy({ it.event }, { it.stateFlow })
        return Fsm(
            transitions = map,
            initial = State(initialState),
            state = State(initialState)
        )
    }
}