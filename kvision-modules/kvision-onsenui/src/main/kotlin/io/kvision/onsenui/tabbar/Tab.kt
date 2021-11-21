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

package io.kvision.onsenui.tabbar

import io.kvision.snabbdom.VNode
import io.kvision.OnsenUIModule.ons
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.Component
import io.kvision.core.Widget
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.Page
import io.kvision.panel.SimplePanel
import io.kvision.utils.createInstance

/**
 * A tab component.
 *
 * @constructor Creates a tab component.
 * @param label the label of the tab item
 * @param icon the name of the icon
 * @param activeIcon the name of the icon when the tab is active
 * @param badge display a notification badge on top of the tab
 * @param active whether this tab is active on start
 * @param className CSS class names
 * @param init an initializer extension function
 */
open class Tab(
    label: String? = null,
    icon: String? = null,
    activeIcon: String? = null,
    badge: String? = null,
    active: Boolean? = null,
    className: String? = null,
    init: (Tab.() -> Unit)? = null
) : SimplePanel(className) {

    /**
     *  The label of the tab item.
     */
    var label: String? by refreshOnUpdate(label)

    /**
     *  The name of the icon.
     */
    var icon: String? by refreshOnUpdate(icon)

    /**
     *  The name of the icon when the tab is active.
     */
    var activeIcon: String? by refreshOnUpdate(activeIcon)

    /**
     *  Display a notification badge on top of the tab.
     */
    var badge: String? by refreshOnUpdate(badge)

    /**
     *  Whether this tab is active on start.
     */
    var active: Boolean? by refreshOnUpdate(active)

    internal var content: Widget? = null

    private val idc = "kv_ons_tab_${counter}"

    init {
        useSnabbdomDistinctKey()
        @Suppress("LeakingThis")
        init?.invoke(this)
        counter++
    }

    override fun render(): VNode {
        return render("ons-tab", childrenVNodes())
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        super.buildAttributeSet(attributeSetBuilder)
        attributeSetBuilder.add("page", idc)
        label?.let {
            attributeSetBuilder.add("label", it)
        }
        icon?.let {
            attributeSetBuilder.add("icon", it)
        }
        activeIcon?.let {
            attributeSetBuilder.add("active-icon", it)
        }
        badge?.let {
            attributeSetBuilder.add("badge", it)
        }
        if (active == true) {
            attributeSetBuilder.add("active")
        }
    }

    override fun afterInsert(node: VNode) {
        node.elm.asDynamic().pageLoader = (ons.PageLoader as Any).createInstance<Any>({ _: dynamic, done: dynamic ->
            if (content != null && content?.parent == null) this.parent?.add(content!!)
            if (content?.getElement() != null) {
                done(content?.getElement())
            } else {
                content?.addAfterInsertHook {
                    @Suppress("UnsafeCastFromDynamic")
                    done(it.elm)
                }
            }
        }, { })
    }

    override fun add(child: Component): Tab {
        if (child is Page || child is Navigator) {
            content = child.unsafeCast<Widget>()
        } else {
            super.add(child)
        }
        return this
    }

    companion object {
        internal var counter = 0
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Tabbar.tab(
    label: String? = null,
    icon: String? = null,
    activeIcon: String? = null,
    badge: String? = null,
    active: Boolean? = null,
    className: String? = null,
    init: (Tab.() -> Unit)? = null
): Tab {
    val tab = Tab(label, icon, activeIcon, badge, active, className, init)
    this.add(tab)
    return tab
}
