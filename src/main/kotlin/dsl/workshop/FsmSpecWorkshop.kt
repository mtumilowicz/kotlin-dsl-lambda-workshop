package dsl.workshop

import dsl.answers.State

/*
fields
    * mutable list of transitions, hint: MutableList, TransitionWorkshop, mutableListOf
    * initial state
 */
class FsmSpecWorkshop() {

    /*
    label: fun add

    argument: transition recipe - TransitionSpecWorkshop as a receiver, hint: TransitionSpecWorkshop.() -> Unit
    function in its body creates TransitionWorkshop from recipe, hint: TransitionWorkshop.create
    and adds it to the transitions, hint: transitions.add
     */

    /*
    label: fun build()

    builds FsmWorkshop
    function in its body creates map from transitions: Map<Event, StateFlow> hint: associateBy
     */

    /*
    overload operator invoke to be able to call fsm { add ... add ... build() } to get FsmWorkshop
    hint: FsmSpecWorkshop.() -> Unit
     */
}