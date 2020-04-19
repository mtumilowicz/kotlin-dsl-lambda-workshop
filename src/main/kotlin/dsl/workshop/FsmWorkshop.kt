package dsl.workshop

import dsl.answers.Event
import dsl.answers.State
import dsl.answers.StateFlow

data class FsmWorkshop(
    val transitions: Map<Event, StateFlow>,
    val initial: State,
    val state: State
) {

    companion object {
        @JvmStatic
        fun create(initialState: String, fsmRecipe: FsmSpecWorkshop.() -> Unit): FsmWorkshop {
            return FsmSpecWorkshop(initialState = initialState).apply(fsmRecipe).build()
        }
    }

    fun returnToInitialState(): FsmWorkshop =
        FsmWorkshop(transitions, initial, initial)

    fun fire(event: String): FsmWorkshop {
        return getTransitionFor(event)
            ?.takeIf { it.from == state }
            ?.let { FsmWorkshop(transitions, initial, it.into) }
            ?: this
    }

    operator fun get(event: String): StateFlow? = transitions[Event(event)]

    private fun getTransitionFor(event: String): StateFlow? = get(event)

    override fun toString(): String = "Fsm(transitions=$transitions, initial=$initial, state=$state)"
}