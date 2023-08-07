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
package test.io.kvision.form.upload

import io.kvision.form.upload.BootstrapUploadInput
import io.kvision.jquery.get
import io.kvision.jquery.invoke
import io.kvision.jquery.jQuery
import io.kvision.panel.Root
import io.kvision.test.DomSpec
import kotlinx.browser.document
import kotlin.test.Test

class BootstrapUploadInputSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED)
            val upi = BootstrapUploadInput(multiple = true).apply {
                id = "idti"
            }
            root.add(upi)
            val content = document.getElementById("test")?.let { jQuery(it).find("input.form-control")[1]?.outerHTML }
            assertEqualsHtml(
                "<input class=\"form-control\" id=\"idti\" type=\"file\" multiple=\"true\">",
                content,
                "Should render correct file input control for multiple files"
            )
            upi.multiple = false
            val content2 = document.getElementById("test")?.let { jQuery(it).find("input.form-control")[1]?.outerHTML }
            assertEqualsHtml(
                "<input class=\"form-control\" id=\"idti\" type=\"file\">",
                content2,
                "Should render correct file input control for single file"
            )
        }
    }

}
