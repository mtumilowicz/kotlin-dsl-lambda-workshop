package dsl

data class Transition(val event: Event, val stateFlow: StateFlow) {

    override fun toString(): String {
        return "$event: $stateFlow"
    }
}