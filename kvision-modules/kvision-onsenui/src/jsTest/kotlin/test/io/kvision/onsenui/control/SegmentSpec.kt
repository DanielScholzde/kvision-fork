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
package test.io.kvision.onsenui.control

import io.kvision.html.button
import io.kvision.onsenui.control.segment
import io.kvision.panel.ContainerType
import io.kvision.panel.Root
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test

class SegmentSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = ContainerType.FIXED)
            root.segment {
                button("a")
                button("b")
                button("c")
            }

            val element = document.getElementById("test")
            assertEqualsHtml(
                "<ons-segment class=\"segment\"><button class=\"btn btn-primary segment__item\" type=\"button\"><input class=\"segment__input\" type=\"radio\" value=\"0\" name=\"ons-segment-gen-0\"><div class=\"segment__button\">a</div></button><button class=\"btn btn-primary segment__item\" type=\"button\"><input class=\"segment__input\" type=\"radio\" value=\"1\" name=\"ons-segment-gen-0\"><div class=\"segment__button\">b</div></button><button class=\"btn btn-primary segment__item\" type=\"button\"><input class=\"segment__input\" type=\"radio\" value=\"2\" name=\"ons-segment-gen-0\"><div class=\"segment__button\">c</div></button></ons-segment>",
                element?.innerHTML,
                "Should render Onsen UI segment component"
            )
        }
    }
}
