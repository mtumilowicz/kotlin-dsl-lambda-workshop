package dsl

class TransitionSpec(
    var event: String?,
    var stateFrom: String?,
    var stateTo: String?
) {

    constructor() : this("", "", "")

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
            event = Event(event ?: ""),
            stateFlow = StateFlow(State(stateFrom ?: ""), State(stateTo ?: ""))
        )
    }
}

fun main() {
    val x = TransitionSpec().apply {
        on("coin")
        from("locked")
        into("unlocked")
    }

    println(x.build())
}

fun buildXXX(
    builderAction: TransitionSpec.() -> Unit
) : Transition {
    val sb = TransitionSpec()
    sb.builderAction()
    return sb.build()
}