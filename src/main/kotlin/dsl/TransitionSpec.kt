package dsl

class TransitionSpec(
    private var event: String = "",
    private var stateFrom: String = "",
    private var stateTo: String = ""
) {

    fun on(event: String) {
        this.event = event
    }

    fun from(state: String) {
        stateFrom = state
    }

    fun into(state: String) {
        stateTo = state
    }

    fun build(): Transition {
        return Transition(
            event = Event(event),
            stateFlow = StateFlow.of(stateFrom, stateTo)
        )
    }
}

fun main() {
    println(2 in listOf(2))
}