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

package io.kvision.state

import io.kvision.core.Widget

/**
 *  Returns a sub-store of the original ObservableState
 *  @param extractor an extractor function
 */
fun <S, T> ObservableState<S>.sub(contextWidget: Widget? = null, extractor: (S) -> T): ObservableState<T> {
    val observableValue = ObservableValue(extractor(this.getState()))
    val unsubscribe = this.subscribe { s ->
        val newValue = extractor(s)
        if (observableValue.value != newValue) {
            observableValue.value = newValue
        }
    }
    contextWidget?.let {
        it.addBeforeDisposeHook {
            unsubscribe()
        }
    }
    return observableValue
}
