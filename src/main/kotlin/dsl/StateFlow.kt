package dsl

data class StateFlow(val from: State, val into: State) {

    companion object {
        @JvmStatic
        fun of(from: String, into: String): StateFlow {
            return StateFlow(State(from), State(into))
        }
    }

    override fun toString(): String {
        return "$from->$into"
    }
}