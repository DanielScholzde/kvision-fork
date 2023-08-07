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
package test.io.kvision.onsenui.core

import io.kvision.onsenui.core.page
import io.kvision.onsenui.core.pullHook
import io.kvision.panel.ContainerType
import io.kvision.panel.Root
import io.kvision.utils.px
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test

class PullHookSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = ContainerType.FIXED)
            root.page {
                pullHook {
                    pullHeight = 100.px
                }
            }

            val element = document.getElementById("test")
            assertEqualsHtml(
                "<ons-page class=\"page\"><div class=\"page__background\"></div><div class=\"page__content\" style=\"touch-action: pan-y; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);\"><ons-pull-hook state=\"initial\" height=\"100px\" style=\"display: none; height: 100px; line-height: 100px;\"></ons-pull-hook></div><span></span></ons-page>",
                element?.innerHTML,
                "Should render Onsen UI pull hook component"
            )
        }
    }
}
