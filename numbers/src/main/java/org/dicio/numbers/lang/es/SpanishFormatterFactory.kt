package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.FormatterFactory

class SpanishFormatterFactory : FormatterFactory {
    override fun getLanguage() = "es"
    override fun create() = SpanishFormatter()
}
