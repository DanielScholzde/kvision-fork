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

package io.kvision.onsenui.dialog

import com.github.snabbdom.VNode
import io.kvision.core.AttributeSetBuilder
import io.kvision.core.Container
import io.kvision.core.Display
import io.kvision.core.DomAttribute
import io.kvision.core.Widget
import io.kvision.core.getElementJQuery
import io.kvision.onsenui.BackButtonEvent
import io.kvision.panel.Root
import io.kvision.panel.Root.Companion.addModal
import io.kvision.panel.SimplePanel
import io.kvision.utils.obj
import kotlin.js.Promise

enum class ToastAnimation(override val attributeValue: String) : DomAttribute {
    NONE("none"),
    FADE("fade"),
    LIFT("lift"),
    ASCEND("ascend"),
    FALL("fall"),
    ;

    override val attributeName: String
        get() = "animation"
}

/**
 * A toast component.
 *
 * @constructor Creates a toast component.
 * @param animation the type of animation
 * @param className CSS class names
 * @param init an initializer extension function
 */
open class Toast(
    animation: ToastAnimation? = null,
    className: String? = null,
    init: (Toast.() -> Unit)? = null
) : SimplePanel(className) {

    override var parent: Container? = Root.getFirstRoot()

    /**
     * The type of animation.
     */
    var animation: ToastAnimation? by refreshOnUpdate(animation)

    /**
     * A dynamic property returning whether the toast is visible.
     */
    @Suppress("UnsafeCastFromDynamic")
    val isVisible: Boolean?
        get() = getElement()?.asDynamic()?.visible

    /**
     * Device back button event listener function.
     */
    protected var onDeviceBackButtonCallback: ((BackButtonEvent) -> Unit)? = null

    init {
        this.hide()
        @Suppress("LeakingThis")
        addModal(this)
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    override fun render(): VNode {
        return render("ons-toast", childrenVNodes())
    }

    @Suppress("UnsafeCastFromDynamic")
    override fun afterInsert(node: VNode) {
        if (onDeviceBackButtonCallback != null) {
            getElement()?.asDynamic()?.onDeviceBackButton = onDeviceBackButtonCallback
        }
        this.getElementJQuery()?.on("preshow") { e, _ ->
            this.dispatchEvent("onsPreshow", obj { detail = e })
        }
        this.getElementJQuery()?.on("postshow") { e, _ ->
            this.dispatchEvent("onsPostshow", obj { detail = e })
        }
        this.getElementJQuery()?.on("prehide") { e, _ ->
            this.dispatchEvent("onsPrehide", obj { detail = e })
        }
        this.getElementJQuery()?.on("posthide") { e, _ ->
            this.hide()
            this.dispatchEvent("onsPosthide", obj { detail = e })
        }
    }

    override fun buildAttributeSet(attributeSetBuilder: AttributeSetBuilder) {
        attributeSetBuilder.add(animation)
        super.buildAttributeSet(attributeSetBuilder)
    }

    /**
     * Shows the toast.
     * @param options a parameter object
     */
    open fun showToast(options: dynamic = undefined): Promise<Unit>? {
        this.display = Display.NONE
        this.show()
        @Suppress("UnsafeCastFromDynamic")
        return getElement()?.asDynamic()?.show(options)
    }

    /**
     * Hides the toast.
     * @param options a parameter object
     */
    open fun hideToast(options: dynamic = undefined): Promise<Unit>? {
        @Suppress("UnsafeCastFromDynamic")
        return getElement()?.asDynamic()?.hide(options)
    }

    /**
     * Sets device back button event listener.
     * @param callback an event listener
     */
    open fun onDeviceBackButton(callback: (event: BackButtonEvent) -> Unit) {
        onDeviceBackButtonCallback = callback
        getElement()?.asDynamic()?.onDeviceBackButton = callback
    }

    /**
     * Clears device back button event listener.
     */
    open fun onDeviceBackButtonClear() {
        onDeviceBackButtonCallback = null
        getElement()?.asDynamic()?.onDeviceBackButton = undefined
    }

    override fun clearParent(): Widget {
        this.parent = null
        return this
    }

    override fun getRoot(): Root? {
        return this.parent?.getRoot()
    }

    override fun dispose() {
        super.dispose()
        Root.removeModal(this)
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
@Suppress("unused")
fun Container.toast(
    animation: ToastAnimation? = null,
    className: String? = null,
    init: (Toast.() -> Unit)? = null
): Toast {
    return Toast(animation, className, init)
}
