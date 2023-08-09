/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package test.io.kvision.redux

import io.kvision.html.div
import io.kvision.panel.Root
import io.kvision.panel.SimplePanel
import io.kvision.redux.RAction
import io.kvision.redux.createTypedReduxStore
import io.kvision.state.bind
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test

data class State(val counter: Int)

sealed class StateAction : RAction {
    data object Inc : StateAction()
    data object Dec : StateAction()
}

fun stateReducer(state: State, action: StateAction): State = when (action) {
    is StateAction.Inc -> {
        state.copy(counter = state.counter + 1)
    }

    is StateAction.Dec -> {
        state.copy(counter = state.counter - 1)
    }
}

class StateBindingSpec : DomSpec {

    @Test
    fun stateBinding() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val store = createTypedReduxStore(::stateReducer, State(10))

            val container = SimplePanel()
            container.bind(store) { state ->
                div("${state.counter}")
            }
            root.add(container)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div>10</div></div>",
                element?.innerHTML,
                "Should render initial state of the container"
            )
            store.dispatch(StateAction.Inc)
            assertEqualsHtml(
                "<div><div>11</div></div>",
                element?.innerHTML,
                "Should render changed state of the container"
            )
        }
    }
}
