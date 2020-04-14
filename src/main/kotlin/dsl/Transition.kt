package dsl

data class Transition(val event: Event, val stateFlow: StateFlow) {

    companion object {
        fun create(transitionSpec: TransitionSpec.() -> Unit): Transition {
            return TransitionSpec().apply(transitionSpec)
                .build()
        }
    }

    override fun toString(): String {
        return "$event: $stateFlow"
    }
}