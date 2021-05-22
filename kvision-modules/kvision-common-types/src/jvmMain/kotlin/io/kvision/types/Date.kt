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
package io.kvision.types

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

actual typealias LocalDateTime = LocalDateTime

actual typealias LocalDate = LocalDate

actual typealias LocalTime = LocalTime

actual typealias OffsetDateTime = OffsetDateTime

actual typealias OffsetTime = OffsetTime

actual typealias ZonedDateTime = ZonedDateTime

fun String.toDateTimeF(): LocalDateTime = LocalDateTime.parse(this)

fun String.toDateF(): LocalDate = LocalDate.parse(this)

fun String.toTimeF(): LocalTime = LocalTime.parse(this)

fun String.toOffsetDateTimeF(): OffsetDateTime = OffsetDateTime.parse(this)

fun String.toOffsetTimeF(): OffsetTime = OffsetTime.parse(this)

fun String.toZonedDateTimeF(): ZonedDateTime = ZonedDateTime.parse(this)

fun LocalDateTime.toStringF(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

fun LocalDate.toStringF(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE)

fun LocalTime.toStringF(): String = this.format(DateTimeFormatter.ISO_LOCAL_TIME)

fun OffsetDateTime.toStringF(): String = this.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

fun OffsetTime.toStringF(): String = this.format(DateTimeFormatter.ISO_OFFSET_TIME)

fun ZonedDateTime.toStringF(): String = this.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
