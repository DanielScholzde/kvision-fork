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
package io.kvision.table

import io.kvision.snabbdom.VNode
import io.kvision.snabbdom.h
import io.kvision.core.ClassSetBuilder
import io.kvision.core.Component
import io.kvision.core.Container
import io.kvision.core.CssClass
import io.kvision.html.TAG
import io.kvision.html.Tag
import io.kvision.panel.SimplePanel
import io.kvision.utils.snClasses
import io.kvision.utils.snOpt

/**
 * HTML table color variants.
 */
enum class TableColor(override val className: String) : CssClass {
    PRIMARY("table-primary"),
    SECONDARY("table-secondary"),
    SUCCESS("table-success"),
    DANGER("table-danger"),
    WARNING("table-warning"),
    INFO("table-info"),
    LIGHT("table-light"),
    DARK("table-dark")
}

/**
 * HTML table types.
 */
enum class TableType(val type: String) {
    STRIPED("table-striped"),
    BORDERED("table-bordered"),
    BORDERLESS("table-borderless"),
    HOVER("table-hover"),
    SMALL("table-sm")
}

/**
 * HTML table responsive types.
 */
enum class ResponsiveType(val type: String) {
    RESPONSIVE("table-responsive"),
    RESPONSIVESM("table-responsive-sm"),
    RESPONSIVEMD("table-responsive-md"),
    RESPONSIVELG("table-responsive-lg"),
    RESPONSIVEXL("table-responsive-xl"),
    RESPONSIVEXXL("table-responsive-xxl")
}

/**
 * HTML table component.
 *
 * @constructor
 * @param headerNames a list of table headers names
 * @param types a set of table types
 * @param caption table caption
 * @param responsiveType determines if the table is responsive
 * @param tableColor table color variant
 * @param theadColor table header color variant
 * @param className CSS class names
 * @param init an initializer extension function
 */
@Suppress("TooManyFunctions")
open class Table(
    headerNames: List<String>? = null,
    types: Set<TableType> = setOf(), caption: String? = null, responsiveType: ResponsiveType? = null,
    tableColor: TableColor? = null, val theadColor: TableColor? = null,
    className: String? = null, init: (Table.() -> Unit)? = null
) : SimplePanel((className?.let { "$it " } ?: "") + "table") {

    /**
     * Table headers names.
     */
    var headerNames by refreshOnUpdate(headerNames) { refreshHeaders() }

    /**
     * Table types.
     */
    var types by refreshOnUpdate(types)

    /**
     * Table caption.
     */
    var caption by refreshOnUpdate(caption)

    /**
     * Determines if the table is responsive.
     */
    var responsiveType by refreshOnUpdate(responsiveType)

    /**
     * Table color variant.
     */
    var tableColor by refreshOnUpdate(tableColor)

    internal val theadRow = Tag(TAG.TR)
    private val thead = Tag(TAG.THEAD).apply {
        if (theadColor != null) addCssClass(theadColor.className)
        add(this@Table.theadRow)
    }
    private val tbody = Tag(TAG.TBODY)

    init {
        @Suppress("LeakingThis")
        thead.parent = this
        @Suppress("LeakingThis")
        tbody.parent = this
        refreshHeaders()
        @Suppress("LeakingThis")
        init?.invoke(this)
    }

    private fun refreshHeaders() {
        theadRow.removeAll()
        headerNames?.forEach {
            theadRow.add(HeaderCell(it, scope = Scope.COL))
        }
    }

    /**
     * Adds new header cell to the table.
     * @param cell header cell
     * @return this table
     */
    fun addHeaderCell(cell: HeaderCell): Table {
        theadRow.add(cell)
        return this
    }

    /**
     * Removes given header cell from the table.
     * @param cell header cell
     * @return this table
     */
    fun removeHeaderCell(cell: HeaderCell): Table {
        theadRow.remove(cell)
        return this
    }

    /**
     * Removes all header cells from table.
     * @return this table
     */
    fun removeHeaderCells(): Table {
        theadRow.removeAll()
        return this
    }

    override fun render(): VNode {
        return if (responsiveType != null) {
            val opt = snOpt {
                `class` = snClasses(listOf(responsiveType!!.type to true))
            }
            h("div", opt, arrayOf(render("table", childrenVNodes())))
        } else {
            render("table", childrenVNodes())
        }
    }

    override fun childrenVNodes(): Array<VNode> {
        val captionElement = caption?.let {
            Tag(TAG.CAPTION, it)
        }
        return listOf(captionElement, thead, tbody).mapNotNull { it?.renderVNode() }.toTypedArray()
    }

    override fun buildClassSet(classSetBuilder: ClassSetBuilder) {
        super.buildClassSet(classSetBuilder)
        types.forEach {
            classSetBuilder.add(it.type)
        }
        classSetBuilder.add(tableColor)
    }

    override fun add(child: Component): Table {
        tbody.add(child)
        return this
    }

    override fun add(position: Int, child: Component): Table {
        tbody.add(position, child)
        return this
    }

    override fun addAll(children: List<Component>): Table {
        tbody.addAll(children)
        return this
    }

    override fun remove(child: Component): Table {
        tbody.remove(child)
        return this
    }

    override fun removeAt(position: Int): Table {
        tbody.removeAt(position)
        return this
    }

    override fun removeAll(): Table {
        tbody.removeAll()
        return this
    }

    override fun disposeAll(): Container {
        tbody.disposeAll()
        return this
    }

    override fun getChildren(): List<Component> {
        return tbody.getChildren()
    }

    override fun dispose() {
        super.dispose()
        thead.dispose()
        tbody.dispose()
    }
}

/**
 * DSL builder extension function.
 *
 * It takes the same parameters as the constructor of the built component.
 */
fun Container.table(
    headerNames: List<String>? = null,
    types: Set<TableType> = setOf(), caption: String? = null, responsiveType: ResponsiveType? = null,
    tableColor: TableColor? = null, theadColor: TableColor? = null,
    className: String? = null,
    init: (Table.() -> Unit)? = null
): Table {
    val table =
        Table(headerNames, types, caption, responsiveType, tableColor, theadColor, className, init)
    this.add(table)
    return table
}
