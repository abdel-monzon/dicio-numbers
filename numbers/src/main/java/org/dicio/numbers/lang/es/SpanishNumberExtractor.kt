package org.dicio.numbers.lang.es

import org.dicio.numbers.parser.lexer.TokenStream
import org.dicio.numbers.unit.Number
import org.dicio.numbers.util.NumberExtractorUtils

class SpanishNumberExtractor internal constructor(
    private val ts: TokenStream,
    private val shortScale: Boolean
) {
    fun numberPreferOrdinal(): Number? {
        val numberExtractor = EnglishNumberExtractor(ts, shortScale) // temporal hasta implementación completa
        return numberExtractor.numberPreferOrdinal()
    }

    fun numberPreferFraction(): Number? {
        val numberExtractor = EnglishNumberExtractor(ts, shortScale) // temporal hasta implementación completa
        return numberExtractor.numberPreferFraction()
    }

    fun numberNoOrdinal(): Number? {
        val numberExtractor = EnglishNumberExtractor(ts, shortScale) // temporal hasta implementación completa
        return numberExtractor.numberNoOrdinal()
    }
}
