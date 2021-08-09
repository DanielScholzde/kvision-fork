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
package test.io.kvision.navbar

import kotlinx.browser.document
import io.kvision.html.TAG
import io.kvision.html.link
import io.kvision.html.tag
import io.kvision.navbar.Nav
import io.kvision.navbar.Navbar
import io.kvision.navbar.NavbarColor
import io.kvision.navbar.NavbarType
import io.kvision.panel.Root
import io.kvision.test.DomSpec
import kotlin.test.Test

class NavbarSpec : DomSpec {

    @Test
    fun render() {
        run {
            val root = Root("test", containerType = io.kvision.panel.ContainerType.FIXED)
            val navbar = Navbar("TEST", "#", NavbarType.FIXEDTOP)
            root.add(navbar)
            val element = document.getElementById("test")
            val id = navbar.container.id
            assertEqualsHtml(
                "<nav class=\"navbar fixed-top navbar-expand-lg navbar-light bg-light\"><div class=\"container-fluid\"><a class=\"navbar-brand\" href=\"#\">TEST</a><button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#$id\" aria-controls=\"$id\" aria-expanded=\"false\" aria-label=\"Toggle navigation\"><span class=\"navbar-toggler-icon\"></span></button><div class=\"collapse navbar-collapse\" id=\"$id\"></div></div></nav>",
                element?.innerHTML,
                "Should render correct navbar"
            )
            navbar.nColor = NavbarColor.DARK
            assertEqualsHtml(
                "<nav class=\"navbar fixed-top navbar-expand-lg bg-light navbar-dark\"><div class=\"container-fluid\"><a class=\"navbar-brand\" href=\"#\">TEST</a><button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#$id\" aria-controls=\"$id\" aria-expanded=\"false\" aria-label=\"Toggle navigation\"><span class=\"navbar-toggler-icon\"></span></button><div class=\"collapse navbar-collapse\" id=\"$id\"></div></div></nav>",
                element?.innerHTML,
                "Should render correct dark navbar"
            )
            navbar.add(Nav {
                tag(TAG.LI) {
                    link("Test", "#!/test")
                }
            })
            assertEqualsHtml(
                "<nav class=\"navbar fixed-top navbar-expand-lg bg-light navbar-dark\"><div class=\"container-fluid\"><a class=\"navbar-brand\" href=\"#\">TEST</a><button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#$id\" aria-controls=\"$id\" aria-expanded=\"false\" aria-label=\"Toggle navigation\"><span class=\"navbar-toggler-icon\"></span></button><div class=\"collapse navbar-collapse\" id=\"$id\"><div class=\"navbar-nav\"><li><a href=\"#!/test\">Test</a></li></div></div></div></nav>",
                element?.innerHTML,
                "Should render correct navbar with nav link"
            )

        }
    }

}
