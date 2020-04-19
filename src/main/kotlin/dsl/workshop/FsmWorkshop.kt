package dsl.workshop

import dsl.answers.Event
import dsl.answers.StateFlow

/*
data class with fields: transitions (Map<Event, StateFlow>), initial, state
 */
class FsmWorkshop() {

    /*
    static fun create
    @JvmStatic - enable using in tests

    creates FsmWorkshop from arguments: initialState and fsmRecipe
    hint: companion object
    hint: FsmSpecWorkshop.() -> Unit, FsmSpecWorkshop, apply
     */

    /*
    fun returnToInitialState()

    return copy with restored initial state as a current state
     */

    /*
    fun fire - move from state to state on event

    hint: getTransitionFor(event), takeIf, let
     */

    /*
    overload operator get to enable fsm[event] syntax
    hint: get(event: String)
     */

    /*
    private fun getTransitionFor

    return appropriate StateFlow
     */
}