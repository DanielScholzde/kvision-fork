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

package io.kvision.onsenui.list

import io.kvision.snabbdom.VNode
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.Color
import io.kvision.core.Component
import io.kvision.html.Div
import io.kvision.panel.SimplePanel

/**
 * List item divider types.
 */
enum class DividerType(internal val type: String) {
    NONE("nodivider"),
    LONG("longdivider")
}

/**
 * An Onsen UI list item component.
 *
 * @constructor Creates a list item component.
 * @param content the content of the center section
 * @param rich whether [content] can contain HTML code
 * @param tappable whether the element reacts to taps
 * @param divider a divider type
 * @param className CSS class names
 * @param init an initializer extension function
 */
@Suppress("LeakingThis")
open class OnsListItem(
    content: String? = null,
    rich: Boolean = false,
    tappable: Boolean? = null,
    divider: DividerType? = null,
    className: String? = null,
    init: (OnsListItem.() -> Unit)? = null
) : SimplePanel(className) {

    /**
     * The left section of the list item.
     */
    val leftPanel = Div(className = "left list-item__left")

    /**
     * The center section of the list item.
     */
    val centerPanel = Div(content, rich, className = "center list-item__center")

    /**
     * The right section of the list item.
     */
    val rightPanel = Div(className = "right list-item__right")

    /**
     * The expandable section of the list item.
     */
    val expandablePanel = Div(className = "expandable-content list-item__expandable-content")

    /**
     *  The content of the center section.
     */
    var content
        get() = centerPanel.content
        set(value) {
            centerPanel.content = value
        }

    /**
     *  Whether [content] can contain HTML code.
     */
    var rich
        get() = centerPanel.rich
        set(value) {
            centerPanel.rich = value
        }

    /**
     * Prevent vertical scrolling when the user drags horizontally.
     */
    var lockOnDrag: Boolean? by refreshOnUpdate()

    /**
     * Whether the element reacts to taps.
     */
    var tappable: Boolean? by refreshOnUpdate(tappable)

    /**
     * The divider type.
     */
    var divider: DividerType? by refreshOnUpdate(divider)

    /**
     * Changes the background color when tapped.
     */
    var tapBackgroundColor: Color? by refreshOnUpdate()

    /**
     * Determines if the transitions are animated.
     */
    var animation: Boolean? by refreshOnUpdate()

    /**
     * A modifier attribute to specify custom styles.
     */
    var modifier: String? by refreshOnUpdate()

    init {
        leftPanel.parent = this
        centerPanel.parent = this
        rightPanel.parent = this
        expandablePanel.parent = this
        init?.invoke(this)
    }

    override fun render(): VNode {
        val leftArr = if (leftPanel.content != null || leftPanel.getChildren().isNotEmpty())
            arrayOf(leftPanel.renderVNode())
        else
            emptyArray()
        val rightArr = if (rightPanel.content != null || rightPanel.getChildren().isNotEmpty())
            arrayOf(rightPanel.renderVNode())
        else
            emptyArray()
        val expandableArr = if (expandablePanel.content != null || expandablePanel.getChildren().isNotEmpty())
            arrayOf(expandablePanel.renderVNode())
        else
            emptyArray()
        return render(
            "ons-list-item",
            leftArr + arrayOf(centerPanel.renderVNode()) + rightArr + expandableArr
        )
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        if (lockOnDrag == true) {
            attributeSetBuilder.add("lock-on-drag")
        }
        if (tappable == true) {
            attributeSetBuilder.add("tappable")
        }
        tapBackgroundColor?.let {
            attributeSetBuilder.add("tap-background-color", it.asString())
        }
        if (expandablePanel.content != null || expandablePanel.getChildren().isNotEmpty()) {
            attributeSetBuilder.add("expandable")
        }
        if (animation == false) {
            attributeSetBuilder.add("animation", "none")
        }
        val modifiers = mutableListOf<String>()
        divider?.let {
            modifiers.add(it.type)
        }
        modifier?.let {
            modifiers.add(it)
        }
        if (modifiers.isNotEmpty()) {
            attributeSetBuilder.add("modifier", modifiers.joinToString(" "))
        }
    }

    override fun add(child: Component) {
        centerPanel.add(child)
    }

    override fun add(position: Int, child: Component) {
        centerPanel.add(position, child)
    }

    override fun addAll(children: List<Component>) {
        centerPanel.addAll(children)
    }

    override fun remove(child: Component) {
        centerPanel.remove(child)
    }

    override fun removeAt(position: Int) {
        centerPanel.removeAt(position)
    }

    override fun removeAll() {
        centerPanel.removeAll()
    }

    override fun disposeAll() {
        centerPanel.disposeAll()
    }

    override fun getChildren(): List<Component> {
        return centerPanel.getChildren()
    }

    /**
     * A DSL builder for the left section of the list item.
     * @param content the content of the left section
     * @param rich whether [content] can contain HTML code
     * @param builder a builder extension function
     */
    open fun left(content: String? = null, rich: Boolean = false, builder: (Div.() -> Unit)? = null) {
        leftPanel.content = content
        leftPanel.rich = rich
        builder?.invoke(leftPanel)
    }

    /**
     * A DSL builder for the center section of the list item.
     * @param content the content of the center section
     * @param rich whether [content] can contain HTML code
     * @param builder a builder extension function
     */
    open fun center(content: String? = null, rich: Boolean = false, builder: (Div.() -> Unit)? = null) {
        centerPanel.content = content
        centerPanel.rich = rich
        builder?.invoke(centerPanel)
    }

    /**
     * A DSL builder for the right section of the list item.
     * @param content the content of the right section
     * @param rich whether [content] can contain HTML code
     * @param builder a builder extension function
     */
    open fun right(content: String? = null, rich: Boolean = false, builder: (Div.() -> Unit)? = null) {
        rightPanel.content = content
        rightPanel.rich = rich
        builder?.invoke(rightPanel)
    }

    /**
     * A DSL builder for the expandable section of the list item.
     * @param content the content of the right section
     * @param rich whether [content] can contain HTML code
     * @param builder a builder extension function
     */
    open fun expandable(content: String? = null, rich: Boolean = false, builder: (Div.() -> Unit)? = null) {
        expandablePanel.content = content
        expandablePanel.rich = rich
        builder?.invoke(expandablePanel)
    }

    /**
     * Shows the expandable content if the element is expandable.
     */
    open fun showExpansion() {
        getElement()?.asDynamic()?.showExpansion()
    }

    /**
     * Hides the expandable content if the element is expandable.
     */
    open fun hideExpansion() {
        getElement()?.asDynamic()?.hideExpansion()
    }

    override fun dispose() {
        super.dispose()
        leftPanel.dispose()
        centerPanel.dispose()
        rightPanel.dispose()
        expandablePanel.dispose()
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun OnsList.item(
    content: String? = null,
    rich: Boolean = false,
    tappable: Boolean? = null,
    divider: DividerType? = null,
    className: String? = null,
    init: (OnsListItem.() -> Unit)? = null
): OnsListItem {
    val onsListItem = OnsListItem(content, rich, tappable, divider, className, init)
    this.add(onsListItem)
    return onsListItem
}
