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

package io.kvision.onsenui.splitter

import com.github.snabbdom.VNode
import io.kvision.OnsenUIModule.ons
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.CssSize
import io.kvision.core.DomAttribute
import io.kvision.onsenui.core.Page
import io.kvision.panel.SimplePanel
import io.kvision.utils.asString
import io.kvision.utils.createInstance
import org.w3c.dom.HTMLElement
import kotlin.js.Promise

/**
 * Splitter side animation types.
 */
enum class SideAnimation(override val attributeValue: String) : DomAttribute {
    OVERLAY("overlay"),
    PUSH("push"),
    REVEAL("reveal"),
    ;

    override val attributeName: String
        get() = "animation"
}

/**
 * Splitter side collapse types.
 */
enum class Collapse(override val attributeValue: String) : DomAttribute {
    COLLAPSE("collapse"),
    PORTRAIT("portrait"),
    LANDSCAPE("landscape"),
    ;

    override val attributeName: String
        get() = "collapse"
}

/**
 * Splitter side positions.
 */
enum class Side(override val attributeValue: String) : DomAttribute {
    LEFT("left"),
    RIGHT("right"),
    ;

    override val attributeName: String
        get() = "side"
}

/**
 * A splitter side component.
 *
 * @constructor Creates a splitter side component.
 * @param animation an animation type
 * @param swipeable whether to enable swipe interaction on collapse mode
 * @param collapse specify the collapse behavior
 * @param side specify which side of the screen the side menu is located
 * @param className CSS class names
 * @param init an initializer extension function
 */
open class SplitterSide(
    animation: SideAnimation? = null,
    swipeable: Boolean? = null,
    collapse: Collapse? = null,
    side: Side? = null,
    className: String? = null,
    init: (SplitterSide.() -> Unit)? = null
) : SimplePanel(className) {

    /**
     * An animation type.
     */
    var animation: SideAnimation? by refreshOnUpdate(animation)

    /**
     * Whether to enable swipe interaction on collapse mode.
     */
    var swipeable: Boolean? by refreshOnUpdate(swipeable)

    /**
     * Specify the collapse behavior.
     */
    var collapse: Collapse? by refreshOnUpdate(collapse)

    /**
     * Specify which side of the screen the side menu is located.
     */
    var side: Side? by refreshOnUpdate(side)

    /**
     * Specify how much the menu needs to be swiped before opening.
     */
    var openThreshold: Number? by refreshOnUpdate()

    /**
     * The width of swipeable area calculated from the edge (in pixels).
     */
    var swipeTargetWidth: Number? by refreshOnUpdate()

    /**
     * The width of swipeable area calculated from the edge (in pixels).
     */
    var sideWidth: CssSize? by refreshOnUpdate()

    /**
     * A dynamic property returning current page.
     */
    @Suppress("UnsafeCastFromDynamic")
    val page: HTMLElement?
        get() = getElement()?.asDynamic()?.page

    /**
     * A dynamic property returning current collapse mode ("split", "collapse", "closed", "open" or "changing").
     */
    @Suppress("UnsafeCastFromDynamic")
    val mode: String?
        get() = getElement()?.asDynamic()?.mode

    /**
     * A dynamic property returning if the side menu is open.
     */
    @Suppress("UnsafeCastFromDynamic")
    val isOpen: Boolean
        get() = getElement()?.asDynamic()?.isOpen ?: false

    /**
     * Swipe event listener function.
     */
    protected var onSwipeCallback: ((Number) -> Unit)? = null

    internal val pagesMap = mutableMapOf<String, Page>()

    init {
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("ons-splitter-side", childrenVNodes())
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        attributeSetBuilder.add(animation)
        if (swipeable == true) {
            attributeSetBuilder.add("swipeable")
        }
        attributeSetBuilder.add(collapse)
        attributeSetBuilder.add(side)
        openThreshold?.let {
            attributeSetBuilder.add("open-threshold", "$it")
        }
        swipeTargetWidth?.let {
            attributeSetBuilder.add("swipe-target-width", "${it}px")
        }
        sideWidth?.let {
            attributeSetBuilder.add("width", it.asString())
        }
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        node.elm.asDynamic().pageLoader = (ons.PageLoader as Any).createInstance<Any>({ o: dynamic, done: dynamic ->
            @Suppress("UnsafeCastFromDynamic")
            val page: Page = o.page
            done(page.getElement())
        }, { })
        if (onSwipeCallback != null) {
            getElement()?.asDynamic()?.onSwipe = onSwipeCallback
        }
    }

    /**
     * Loads the specified page into the splitter side menu.
     * @param pageId a given page id
     * @param options a parameter object
     */
    open fun load(pageId: String, options: dynamic = undefined): Promise<Unit>? {
        return pagesMap[pageId]?.let { load(it, options) }
    }

    /**
     * Loads the specified page into the splitter side menu.
     * @param page a given page
     * @param options a parameter object
     */
    @Suppress("UnsafeCastFromDynamic")
    open fun load(page: Page, options: dynamic = undefined): Promise<Unit>? {
        (children?.first() as? Page)?.let {
            it.dispatchHideEvent()
            it.dispatchDestroyEvent()
            remove(it)
        }
        add(page)
        return getElement()?.asDynamic()?.load(page, options)
    }

    /**
     * Opens side menu.
     * @param options a parameter object
     */
    @Suppress("UnsafeCastFromDynamic")
    open fun open(options: dynamic = undefined): Promise<Unit>? {
        return getElement()?.asDynamic()?.open(options)
    }

    /**
     * Closes side menu.
     * @param options a parameter object
     */
    @Suppress("UnsafeCastFromDynamic")
    open fun close(options: dynamic = undefined): Promise<Unit>? {
        return getElement()?.asDynamic()?.close(options)
    }

    /**
     * Toggles side menu visibility.
     * @param options a parameter object
     */
    @Suppress("UnsafeCastFromDynamic")
    open fun toggle(options: dynamic = undefined): Promise<Unit>? {
        return getElement()?.asDynamic()?.toggle(options)
    }

    /**
     * Sets swipe event listener.
     * @param callback an event listener
     */
    open fun onSwipe(callback: (ratio: Number) -> Unit) {
        onSwipeCallback = callback
        getElement()?.asDynamic()?.onSwipe = callback
    }

    /**
     * Clears swipe event listener.
     */
    open fun onSwipeClear() {
        onSwipeCallback = null
        getElement()?.asDynamic()?.onSwipe = undefined
    }

}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Splitter.splitterSide(
    animation: SideAnimation? = null,
    swipeable: Boolean? = null,
    collapse: Collapse? = null,
    side: Side? = null,
    className: String? = null,
    init: (SplitterSide.() -> Unit)? = null
): SplitterSide {
    val splitterSide =
        SplitterSide(animation, swipeable, collapse, side, className, init)
    this.add(splitterSide)
    return splitterSide
}
