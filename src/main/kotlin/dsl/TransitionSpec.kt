package dsl

class TransitionSpec(
    private var event: String = "",
    private var stateFrom: String = "",
    private var stateTo: String = ""
) {

    fun on(event: String): TransitionSpec {
        this.event = event
        return this
    }

    fun from(state: String): TransitionSpec {
        stateFrom = state
        return this
    }

    fun into(state: String): TransitionSpec {
        stateTo = state
        return this
    }

    fun build(): Transition {
        return Transition(
            event = Event(event),
            stateFlow = StateFlow(State(stateFrom), State(stateTo))
        )
    }
}