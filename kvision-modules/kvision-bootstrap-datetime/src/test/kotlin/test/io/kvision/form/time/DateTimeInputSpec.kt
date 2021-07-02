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
package test.io.kvision.form.time

import io.kvision.form.time.DateTimeInput
import io.kvision.panel.Root
import io.kvision.types.toStringF
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.js.Date
import kotlin.test.Test

class DateTimeInputSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED)
            val data = Date()
            val dti = DateTimeInput(value = data).apply {
                placeholder = "place"
                id = "idti"
            }
            root.add(dti)
            val element = document.getElementById("test")
            val datastr = data.toStringF(dti.format)
            assertEqualsHtml(
                "<div class=\"input-group date\" id=\"idti\"><input class=\"form-control\" placeholder=\"place\" type=\"text\" value=\"$datastr\"><span class=\"input-group-text datepickerbutton\"><span class=\"fas fa-calendar-alt\"></span></span></div>",
                element?.innerHTML,
                "Should render date time input with correctly formatted value"
            )
        }
    }

}
