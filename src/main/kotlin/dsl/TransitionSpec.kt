package dsl

class TransitionSpec(
    private var event: String = "",
    private var stateFrom: String = "",
    private var stateTo: String = ""
) {

    infix fun on(event: String): TransitionSpec {
        this.event = event
        return this
    }

    infix fun from(state: String): TransitionSpec {
        stateFrom = state
        return this
    }

    infix fun into(state: String): TransitionSpec {
        stateTo = state
        return this
    }

    fun build(): Transition {
        return Transition(
            event = Event(event),
            stateFlow = StateFlow(stateFrom, stateTo)
        )
    }
}