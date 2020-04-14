package dsl

class Fsm(
    val transitions: Map<Event, StateFlow>,
    val initial: State,
    val state: State
) {

    companion object {
        @JvmStatic
        fun create(initialState: String, xxx: FsmSpec.() -> FsmSpec): Fsm {
            return FsmSpec(initialState = initialState)
                .xxx()
                .build()
        }
    }

    fun returnToInitialState(): Fsm {
        return Fsm(transitions, initial, initial)
    }

    fun fire(event: Event): Fsm {
        return getTransitionFor(event)
            ?.takeIf { it.from == state }
            ?.let { Fsm(transitions, initial, it.into) }
            ?: this
    }

    operator fun get(event: String): StateFlow? {
        return transitions[Event(event)]
    }

    private fun getTransitionFor(event: Event): StateFlow? {
        return transitions[event]
    }

    override fun toString(): String {
        return "Fsm(transitions=$transitions, initial=$initial, state=$state)"
    }
}

fun main() {
    val fsm = Fsm.create(initialState = "locked") {
        add { on("coin").from("locked").into("unlocked") }
        add { on("pass").from("unlocked").into("locked") }
    }

    println(fsm)
}