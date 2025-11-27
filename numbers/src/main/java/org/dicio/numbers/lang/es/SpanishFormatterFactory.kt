package org.dicio.numbers.lang.es

import org.dicio.numbers.formatter.Formatter
import org.dicio.numbers.formatter.FormatterFactory

class SpanishFormatterFactory : FormatterFactory() {
    override fun getLanguage(): String = "es"
    override fun create(): Formatter = SpanishFormatter()
}
