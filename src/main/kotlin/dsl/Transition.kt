package dsl

data class Transition(val event: Event, val stateFlow: StateFlow) {

    companion object {
        fun create(transitionSpec: TransitionSpec.() -> TransitionSpec): Transition {
            return TransitionSpec()
                .transitionSpec()
                .build()
        }
    }

    override fun toString(): String {
        return "$event: $stateFlow"
    }
}