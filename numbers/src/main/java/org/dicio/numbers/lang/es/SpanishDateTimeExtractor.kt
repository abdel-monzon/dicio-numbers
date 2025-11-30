package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.TokenStream
import org.dicio.numbers.unit.Number
import java.time.LocalDateTime

class SpanishDateTimeExtractor(
    private val shortScale: Boolean,
    private val tokens: TokenStream,
    private val preferMonthBeforeDay: Boolean,
    private val now: LocalDateTime
) {
    fun extract(): LocalDateTime? = null
}
