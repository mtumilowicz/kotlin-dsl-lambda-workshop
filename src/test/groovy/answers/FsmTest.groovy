package answers

import dsl.Fsm
import dsl.State
import dsl.StateFlow
import spock.lang.Specification

class FsmTest extends Specification {

    def 'create empty fsm'() {

        when:
        def fsm = Fsm.create("initial") {
        }

        then:
        fsm.initial == new State("initial")
        fsm.transitions.size() == 0
    }


    def "create fsm with initial state and two transitions"() {

        when:
        def fsm = Fsm.create(_state0) {
            it.add { it.on _event1 from _state1 into _state2 }
            it.add { it.on _event2 from _state2 into _state3 }
        }

        then:
        fsm.initial == new State(_state0)
        fsm.transitions.size() == 2
        fsm[_event1] == new StateFlow(_state1, _state2)
        fsm[_event2] == new StateFlow(_state2, _state3)

        where:
        _event1  | _event2  | _state0  | _state1  | _state2  | _state3
        "event1" | "event2" | "state0" | "state1" | "state2" | "state3"
    }

    def "order of on-from-into inside add is not important"() {

        given: "event1"
        def _event1 = 'event1'
        def _event1From = "${_event1}From"
        def _event1Into = "${_event1}Into"

        and: 'event2'
        def _event2 = 'event2'
        def _event2From = "${_event2}From"
        def _event2Into = "${_event2}Into"

        and: 'event3'
        def _event3 = 'event3'
        def _event3From = "${_event3}From"
        def _event3Into = "${_event3}Into"

        and: 'event4'
        def _event4 = 'event4'
        def _event4From = "${_event4}From"
        def _event4Into = "${_event4}Into"

        when: 'on-from-into'
        def fsm = Fsm.create("initialState") {
            it.add { it.on _event1 from _event1From into _event1Into } // it.on from into
            it.add { it.from _event2From on _event2 into _event2Into } // from it.on into
            it.add { it.from _event3From into _event3Into on _event3 } // from into on
            it.add { it.into _event4Into from _event4From on _event4 } // into from on
        }

        then: 'outcome is always the same'
        fsm[_event1] == new StateFlow(_event1From, _event1Into)
        fsm[_event2] == new StateFlow(_event2From, _event2Into)
        fsm[_event3] == new StateFlow(_event3From, _event3Into)
        fsm[_event4] == new StateFlow(_event4From, _event4Into)

    }

    def "if event is not defined in transitions, stay in current state"() {

        given:
        def _initialState = 'initialState'

        and: 'create fsm with empty transitions'
        def fsm = Fsm.create(_initialState) {
        }

        when:
        fsm = fsm.fire('event')

        then:
        fsm.state == new State(_initialState)
    }

    def "if transition is not designed for current state - stay in current state"() {

        given:
        def _state = 'state'
        def _event = 'event'

        and: 'create fsm with single transition not designed for state'
        def fsm = Fsm.create(_state) {
            it.add { it.on _event from 'stateFrom' into 'stateTo' }
        }

        when: 'transition is not designed for current state'
        fsm = fsm.fire(_event)

        then: 'stay in current state'
        fsm.state == new State(_state)
    }

    def "if transition is designed for current state - move to stateInto"() {

        given:
        def _state = 'state'

        and: 'define transition'
        def _event = 'event'
        def _stateTo = 'stateTo'

        and: 'create fsm with single transition designed for state'
        def fsm = Fsm.create(_state) {
            it.add { it.on _event from _state into _stateTo }
        }

        when: 'transition is designed for current state'
        fsm = fsm.fire(_event)

        then: 'moved to stateTo'
        fsm.state == new State(_stateTo)
    }

    def "if we are in initial state then returning to initial state means returning exact copy"() {

        given:
        def fsm = Fsm.create('state') {
        }

        expect:
        fsm == fsm.returnToInitialState()
    }

    def "if we are in any state then returning to initial state means returning copy with current state set to initial state"() {

        given: 'create fsm with single transition'
        def fsm = Fsm.create('initialState') {
            it.add { it.on 'event' from 'initialState' into 'state2' }
        }

        and: 'state changes to state2'
        def fsmInState2 = fsm.fire('event')

        when: 'return to initial state'
        def fsmInInitialState = fsmInState2.returnToInitialState()

        then:
        with(fsmInInitialState) {
            initial == new State('initialState')
            transitions == fsmInState2.transitions
        }
    }
}


