package dsl.workshop

import dsl.answers.Event
import dsl.answers.StateFlow

data class TransitionWorkshop(val event: Event, val stateFlow: StateFlow) {

    companion object {
        fun create(transitionSpec: TransitionSpecWorkshop.() -> Unit): TransitionWorkshop {
            return TransitionSpecWorkshop().apply(transitionSpec)
                .build()
        }
    }

    override fun toString(): String = "$event: $stateFlow"
}