package dsl.answers

data class Fsm(
    val transitions: Map<Event, StateFlow>,
    val initial: State,
    val state: State
) {

    companion object {
        @JvmStatic
        fun create(initialState: String, fsmRecipe: FsmSpec.() -> Unit): Fsm {
            return FsmSpec(initialState = initialState).apply(fsmRecipe).build()
        }
    }

    fun returnToInitialState(): Fsm =
        Fsm(transitions, initial, initial)

    fun fire(event: String): Fsm {
        return getTransitionFor(event)
            ?.takeIf { it.from == state }
            ?.let { Fsm(transitions, initial, it.into) }
            ?: this
    }

    operator fun get(event: String): StateFlow? = transitions[Event(event)]

    private fun getTransitionFor(event: String): StateFlow? = get(event)

    override fun toString(): String = "Fsm(transitions=$transitions, initial=$initial, state=$state)"
}