package org.dicio.numbers.lang.es

import org.dicio.numbers.unit.NumberExtractor
import org.dicio.numbers.unit.Number

class SpanishNumberExtractor : NumberExtractor {
    override fun getLanguage() = "es"
    override fun extract(text: String): List<Number> = emptyList()
}
