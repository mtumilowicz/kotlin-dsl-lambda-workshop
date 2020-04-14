package dsl

class StateFlow(val from: State, val into: State) {

    override fun toString(): String {
        return "$from->$into"
    }
}