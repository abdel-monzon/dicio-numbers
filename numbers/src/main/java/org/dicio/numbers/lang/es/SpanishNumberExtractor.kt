package org.dicio.numbers.lang.es

import org.dicio.numbers.unit.NumberExtractor

class SpanishNumberExtractor : NumberExtractor {
    override fun getLanguage() = "es"
    override fun extract(text: String): List<Number> = emptyList()
}
