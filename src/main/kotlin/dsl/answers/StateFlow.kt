package dsl.answers

data class StateFlow(
    val from: State,
    val into: State
) {

    constructor(from: String, into: String) : this(
        State(from),
        State(into)
    )

    override fun toString(): String = "$from->$into"
}