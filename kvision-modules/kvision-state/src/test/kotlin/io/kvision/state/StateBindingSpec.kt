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
package test.io.kvision.state

import io.kvision.html.div
import io.kvision.panel.Root
import io.kvision.panel.SimplePanel
import io.kvision.panel.simplePanel
import io.kvision.state.ObservableValue
import io.kvision.state.bind
import io.kvision.state.bindEach
import io.kvision.state.observableListOf
import io.kvision.state.observableSetOf
import io.kvision.state.sub
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

data class ComplexStore(val a: Int, val b: String, val c: Boolean)

class StateBindingSpec : DomSpec {

    @Test
    fun observableValue() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val observableValue = ObservableValue(1)
            root.simplePanel().bind(observableValue) { state ->
                div("$state")
            }
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div>1</div></div>",
                element?.innerHTML,
                "Should render initial value"
            )
            observableValue.value++
            assertEqualsHtml(
                "<div><div>2</div></div>",
                element?.innerHTML,
                "Should render changed value"
            )
        }
    }

    @Test
    fun sub() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val observableValue = ObservableValue(ComplexStore(1, "2", true))
            var counter = 0
            root.simplePanel().bind(observableValue, { it.a }) { state ->
                counter++
                div("$state")
            }
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div>1</div></div>",
                element?.innerHTML,
                "Should render initial value"
            )
            observableValue.value = observableValue.value.copy(b = "3")
            assertEqualsHtml(
                "<div><div>1</div></div>",
                element?.innerHTML,
                "Should render the same value"
            )
            assertEquals(1, counter, "The content should not be rendered twice")
        }
    }

    @Test
    fun subSet() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val observableSet = observableSetOf(1, 2, 3)
            var counter = 0
            root.simplePanel().bind(observableSet, { it.minus(1) }) { state ->
                counter++
                div("$state")
            }
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div>[2, 3]</div></div>",
                element?.innerHTML,
                "Should render initial set value"
            )
            observableSet.add(4)
            assertEqualsHtml(
                "<div><div>[2, 3, 4]</div></div>",
                element?.innerHTML,
                "Should render changed set value"
            )
            assertEquals(2, counter, "The content should be rendered again")
            observableSet.remove(1)
            assertEqualsHtml(
                "<div><div>[2, 3, 4]</div></div>",
                element?.innerHTML,
                "Should render the same set value"
            )
            assertEquals(2, counter, "The content should not be rendered again")
        }
    }

    @Test
    fun bind() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val container = SimplePanel()
            val observableList = observableListOf(1, 2, 3)
            container.bind(observableList) { state ->
                setAttribute("data-count", "${state.size}")
                state.forEach {
                    div("$it")
                }
            }
            root.add(container)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div data-count=\"3\"><div>1</div><div>2</div><div>3</div></div>",
                element?.innerHTML,
                "Should render initial state of the widget"
            )
            observableList.add(4)
            assertEqualsHtml(
                "<div data-count=\"4\"><div>1</div><div>2</div><div>3</div><div>4</div></div>",
                element?.innerHTML,
                "Should render changed state of the widget"
            )
        }
    }

    @Test
    fun bindNotImmediately() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val container = SimplePanel()
            val observableList = observableListOf(1, 2, 3)
            container.bind(observableList, runImmediately = false) { state ->
                setAttribute("data-count", "${state.size}")
                state.forEach {
                    div("$it")
                }
            }
            root.add(container)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div></div>",
                element?.innerHTML,
                "Should render empty widget"
            )
            observableList.add(4)
            assertEqualsHtml(
                "<div data-count=\"4\"><div>1</div><div>2</div><div>3</div><div>4</div></div>",
                element?.innerHTML,
                "Should render new state of the widget"
            )
        }
    }

    @Test
    fun bindEach() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val container = SimplePanel()
            val observableList = observableListOf(1, 2, 3)
            container.bindEach(observableList) { state ->
                div("$state")
            }
            root.add(container)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div>1</div><div>2</div><div>3</div></div>",
                element?.innerHTML,
                "Should render initial state of the list of items"
            )
            observableList.add(1, 4)
            assertEqualsHtml(
                "<div><div>1</div><div>4</div><div>2</div><div>3</div></div>",
                element?.innerHTML,
                "Should render changed state of the list of items"
            )
        }
    }

    @Test
    fun bindEachMultiple() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED) {
                synchronousMode = true
            }
            val container = SimplePanel()
            val observableList = observableListOf(1, 2, 3)
            container.bindEach(observableList) { state ->
                div("$state")
                div("${state + 10}")
            }
            root.add(container)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<div><div style=\"display: contents;\"><div>1</div><div>11</div></div><div style=\"display: contents;\"><div>2</div><div>12</div></div><div style=\"display: contents;\"><div>3</div><div>13</div></div></div>",
                element?.innerHTML,
                "Should render initial state of the list of items"
            )
            observableList.add(1, 4)
            assertEqualsHtml(
                "<div><div style=\"display: contents;\"><div>1</div><div>11</div></div><div style=\"display: contents;\"><div>4</div><div>14</div></div><div style=\"display: contents;\"><div>2</div><div>12</div></div><div style=\"display: contents;\"><div>3</div><div>13</div></div></div>",
                element?.innerHTML,
                "Should render changed state of the list of items"
            )
        }
    }
}
