package dsl.workshop

import dsl.answers.Event
import dsl.answers.StateFlow

class TransitionSpecWorkshop(
    private var event: String = "",
    private var stateFrom: String = "",
    private var stateTo: String = ""
) {

    infix fun on(event: String): TransitionSpecWorkshop {
        this.event = event
        return this
    }

    infix fun from(state: String): TransitionSpecWorkshop {
        stateFrom = state
        return this
    }

    infix fun into(state: String): TransitionSpecWorkshop {
        stateTo = state
        return this
    }

    fun build(): TransitionWorkshop {
        return TransitionWorkshop(
            event = Event(event),
            stateFlow = StateFlow(stateFrom, stateTo)
        )
    }
}