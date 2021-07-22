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
package io.kvision

import io.kvision.i18n.I18n
import io.kvision.utils.obj
import kotlinx.browser.window

/**
 * Internal singleton object which initializes and configures KVision RichText module.
 */
internal object KVManagerRichText {

    init {
        require("trix/dist/trix.css")
        val trix = require("trix/dist/trix-core.js")
        window.asDynamic().Trix = trix
        trix.config.languages = obj {}
        trix.config.languages["en"] = obj {}
        for (key in js("Object").keys(trix.config.lang)) {
            trix.config.languages["en"][key] = trix.config.lang[key]
        }
        val orig = trix.config.toolbar.getDefaultHTML
        trix.config.toolbar.getDefaultHTML = {
            val config = if (trix.config.languages[I18n.language] != undefined) {
                trix.config.languages[I18n.language]
            } else {
                trix.config.languages["en"]
            }
            for (key in js("Object").keys(trix.config.lang)) {
                trix.config.lang[key] = config[key]
            }
            orig()
        }
        require("kvision-assets/js/locales/trix/trix.pl.js")
    }

    internal fun init() {}
}
