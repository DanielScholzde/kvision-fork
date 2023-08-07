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

package io.kvision.onsenui.visual

import io.kvision.snabbdom.VNode
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.Container
import io.kvision.core.Widget

/**
 * A progress bar component.
 *
 * @constructor Creates a progress bar component.
 * @param value the current progress (should be a value between 0 and 100)
 * @param secondaryValue the current secondary progress (should be a value between 0 and 100)
 * @param indeterminate whether infinite looping animation is shown
 * @param className CSS class names
 * @param init an initializer extension function
 */
open class ProgressBar(
    value: Number? = null,
    secondaryValue: Number? = null,
    indeterminate: Boolean? = null,
    className: String? = null,
    init: (ProgressBar.() -> Unit)? = null
) : Widget(className) {

    /**
     * The current progress (should be a value between 0 and 100).
     */
    var value: Number? by refreshOnUpdate(value)

    /**
     * The current secondary progress (should be a value between 0 and 100).
     */
    var secondaryValue: Number? by refreshOnUpdate(secondaryValue)

    /**
     * Whether infinite looping animation is shown.
     */
    var indeterminate: Boolean? by refreshOnUpdate(indeterminate)

    /**
     * A modifier attribute to specify custom styles.
     */
    var modifier: String? by refreshOnUpdate()

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("ons-progress-bar")
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        value?.let {
            attributeSetBuilder.add("value", "$it")
        }
        secondaryValue?.let {
            attributeSetBuilder.add("secondary-value", "$it")
        }
        if (indeterminate == true) {
            attributeSetBuilder.add("indeterminate")
        }
        modifier?.let {
            attributeSetBuilder.add("modifier", it)
        }
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.progressBar(
    value: Number? = null,
    secondaryValue: Number? = null,
    indeterminate: Boolean? = null,
    className: String? = null,
    init: (ProgressBar.() -> Unit)? = null
): ProgressBar {
    val progressBar = ProgressBar(value, secondaryValue, indeterminate, className, init)
    this.add(progressBar)
    return progressBar
}
