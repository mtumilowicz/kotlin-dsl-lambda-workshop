package dsl

class Fsm(
    val transitions: Map<Event, StateFlow>,
    val initial: State,
    val state: State
) {

    companion object {
        fun create(initialState: String, fsmRecipe: FsmSpec.() -> Unit): Fsm {
            return FsmSpec(initialState = initialState).apply(fsmRecipe).build()
        }
    }

    fun returnToInitialState(): Fsm = Fsm(transitions, initial, initial)

    fun fire(event: Event): Fsm {
        return getTransitionFor(event)
            ?.takeIf { it.from == state }
            ?.let { Fsm(transitions, initial, it.into) }
            ?: this
    }

    operator fun get(event: String): StateFlow? = transitions[Event(event)]

    private fun getTransitionFor(event: Event): StateFlow? = transitions[event]

    override fun toString(): String = "Fsm(transitions=$transitions, initial=$initial, state=$state)"
}