package dsl

data class Event(val raw: String) {

    override fun toString(): String {
        return raw
    }
}