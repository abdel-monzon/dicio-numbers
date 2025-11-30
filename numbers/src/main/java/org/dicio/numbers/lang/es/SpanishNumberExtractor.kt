package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.lexer.TokenStream
import org.dicio.numbers.unit.Number
import org.dicio.numbers.util.NumberExtractorUtils

class SpanishNumberExtractor internal constructor(
    private val ts: TokenStream,
    private val shortScale: Boolean
) {
    fun numberPreferOrdinal(): Number? = numberSignPoint(true)
    fun numberPreferFraction(): Number? = numberSignPoint(true)
    fun numberNoOrdinal(): Number? = numberSignPoint(false)

    fun numberSignPoint(allowOrdinal: Boolean): Number? {
        return NumberExtractorUtils.signBeforeNumber(ts) { numberInteger(allowOrdinal) }
    }

    fun numberInteger(allowOrdinal: Boolean): Number? {
        return NumberExtractorUtils.numberLessThan1000(ts, allowOrdinal)
    }
}
