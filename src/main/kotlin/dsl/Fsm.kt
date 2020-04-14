package dsl

class Fsm(
    val transitions: Map<Event, StateFlow>,
    val initial: State,
    val state: State
) {

    companion object {
        fun create(initialState: String, fsmRecipe: FsmSpec.() -> FsmSpec): Fsm {
            return FsmSpec(initialState = initialState)
                .fsmRecipe()
                .build()
        }
    }

    fun returnToInitialState(): Fsm {
        return Fsm(transitions, initial, initial)
    }

    fun fire(event: Event): Fsm {
        return getTransitionFor(event)
            ?.takeIf { it.from == state }
            ?.let { Fsm(transitions, initial, it.into) }
            ?: this
    }

    operator fun get(event: String): StateFlow? {
        return transitions[Event(event)]
    }

    private fun getTransitionFor(event: Event): StateFlow? {
        return transitions[event]
    }

    override fun toString(): String {
        return "Fsm(transitions=$transitions, initial=$initial, state=$state)"
    }
}