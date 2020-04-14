package dsl

class FsmSpec(
    private val transitions: MutableList<TransitionSpec.() -> TransitionSpec> = mutableListOf(),
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