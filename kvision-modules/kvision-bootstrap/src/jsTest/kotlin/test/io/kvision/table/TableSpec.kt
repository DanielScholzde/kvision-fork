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
package test.io.kvision.table

import io.kvision.panel.Root
import io.kvision.table.ResponsiveType
import io.kvision.table.Table
import io.kvision.table.TableType
import io.kvision.table.cell
import io.kvision.table.row
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test

class TableSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED)
            val table = Table(listOf("a", "b")) {
                row {
                    cell("A")
                    cell("B")
                }
            }
            root.add(table)
            val element = document.getElementById("test")
            assertEqualsHtml(
                "<table class=\"table\"><thead><tr><th scope=\"col\">a</th><th scope=\"col\">b</th></tr></thead><tbody><tr><td>A</td><td>B</td></tr></tbody></table>",
                element?.innerHTML,
                "Should render correct table"
            )
            table.caption = "Caption"
            table.responsiveType = ResponsiveType.RESPONSIVE
            table.types = setOf(TableType.BORDERED)
            val element2 = document.getElementById("test")
            assertEqualsHtml(
                "<div class=\"table-responsive\"><table class=\"table table-bordered\"><caption>Caption</caption><thead><tr><th scope=\"col\">a</th><th scope=\"col\">b</th></tr></thead><tbody><tr><td>A</td><td>B</td></tr></tbody></table></div>",
                element2?.innerHTML,
                "Should render correct responsive table"
            )
        }
    }

}
